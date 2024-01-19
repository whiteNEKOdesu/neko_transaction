package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.ProductApplyStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.entity.ProductApplyInfo;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductApplyInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.service.ProductApplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.service.ProductImageService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.vo.NewProductApplyInfoVo;
import neko.transaction.product.vo.ProductApplyInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 商品上架申请信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Service
public class ProductApplyInfoServiceImpl extends ServiceImpl<ProductApplyInfoMapper, ProductApplyInfo> implements ProductApplyInfoService {
    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private ProductImageService productImageService;

    @Resource
    private OSSFeignService ossFeignService;

    /**
     * 添加商品上架申请信息
     * @param vo 商品上架申请信息的vo
     */
    @Override
    public void newProductApplyInfo(NewProductApplyInfoVo vo) {
        CategoryInfo categoryInfo = categoryInfoService.getById(vo.getCategoryId());
        if(categoryInfo == null || !categoryInfo.getLevel().equals(1)){
            throw new IllegalArgumentException("分类id不存在或不为叶节点");
        }

        //step1 -> 上传图片到oss
        ResultObject<String> r = ossFeignService.uploadImage(vo.getApplyImage());
        if(!r.getResponseCode().equals(200)){
            throw new ThirdPartyServiceException("thirdparty微服务远程调用异常");
        }


        //step2 -> 添加商品上架申请信息
        ProductApplyInfo productApplyInfo = new ProductApplyInfo();
        BeanUtil.copyProperties(vo, productApplyInfo);
        //获取全分类名
        String fullCategoryName = categoryInfoService.getFullCategoryName(vo.getCategoryId());
        //设置上传的图片url
        productApplyInfo.setApplyImage(r.getResult())
                .setFullCategoryName(fullCategoryName)
                //设置学号
                .setUid((String) StpUtil.getLoginId())
                //设置价格精度
                .setPrice(productApplyInfo.getPrice()
                        .setScale(2, BigDecimal.ROUND_DOWN));

        //添加商品上架申请信息到商品上架申请信息表
        this.baseMapper.insert(productApplyInfo);
    }

    /**
     * 分页查询未处理的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductApplyInfoVo> unhandledApplyPageQuery(QueryVo vo) {
        Page<ProductApplyInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        //设置分页查询结果
        page.setRecords(this.baseMapper.unhandledApplyPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords()));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.unhandledApplyPageQueryNumber(vo.getQueryWords()));

        return page;
    }

    /**
     * 通过商品上架申请
     * @param productApplyId 申请id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passApply(String productApplyId) {
        ProductApplyInfo productApplyInfo = this.baseMapper.selectById(productApplyId);
        if(productApplyInfo == null){
            throw new NoSuchResultException("无此申请信息");
        }

        //step1 -> 修改申请状态为通过
        if(this.baseMapper.updateUnhandledApplyStatus(productApplyId, ProductApplyStatus.PASSED) != 1){
            return;
        }

        //step2 -> 添加商品信息到商品信息表
        ProductInfo productInfo = new ProductInfo();
        BeanUtil.copyProperties(productApplyInfo, productInfo);
        productInfoService.save(productInfo);

        //step3 -> 添加商品图片信息到商品图片信息表
        ProductImage productImage = new ProductImage();
        //设置商品id
        productImage.setProductId(productInfo.getProductId())
                //设置商品图片url
                .setProductImage(productApplyInfo.getApplyImage());
        productImageService.save(productImage);
    }

    /**
     * 拒绝商品上架申请
     * @param productApplyId 申请id
     */
    @Override
    public void rejectApply(String productApplyId) {
        ProductApplyInfo productApplyInfo = this.baseMapper.selectById(productApplyId);
        if(productApplyInfo == null){
            throw new NoSuchResultException("无此申请信息");
        }

        //修改状态为未通过
        this.baseMapper.updateUnhandledApplyStatus(productApplyId, ProductApplyStatus.REJECTED);
    }

    /**
     * 分页查询学生自身的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductApplyInfoVo> userSelfApplyPageQuery(QueryVo vo) {
        Page<ProductApplyInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = (String) StpUtil.getLoginId();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.userSelfApplyPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.userSelfApplyPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }
}
