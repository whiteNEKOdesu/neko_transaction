package neko.transaction.product.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.ProductStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.elasticsearch.service.ProductInfoESService;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.to.MemberInfoTo;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Service
@Slf4j
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private ProductInfoESService productInfoESService;

    @Resource
    private OSSFeignService ossFeignService;

    @Resource
    private MemberInfoFeignService memberInfoFeignService;

    /**
     * 分页查询学生自身的商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductInfoVo> userSelfPageQuery(QueryVo vo) {
        Page<ProductInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = (String) StpUtil.getLoginId();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.userSelfPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.userSelfPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    @Override
    public ProductInfoVo getUserSelfProductInfoById(String productId) {
        return this.baseMapper.getUserSelfProductInfoById(productId, (String) StpUtil.getLoginId());
    }

    /**
     * 修改商品信息
     * @param vo 修改商品信息vo
     */
    @Override
    public void updateProductInfo(UpdateProductInfoVo vo) {
        if(vo.getProductName() == null && vo.getDescription() == null &&
                vo.getCategoryId() == null && vo.getPrice() == null && vo.getDisplayImage() == null){
            return;
        }

        String productId = vo.getProductId();
        ProductInfo productInfo = this.baseMapper.selectById(productId);
        if(productInfo == null){
            throw new IllegalArgumentException("productId对应商品信息不存在");
        }
        //商品不属于该用户
        if(!productInfo.getUid().equals(StpUtil.getLoginId())){
            throw new NotPermissionException("访问商品信息权限不足");
        }

        if(vo.getCategoryId() != null){
            CategoryInfo categoryInfo = categoryInfoService.getById(vo.getCategoryId());
            if(categoryInfo != null && !categoryInfo.getLevel().equals(1)){
                throw new IllegalArgumentException("分类id对应的分类不为叶节点");
            }
        }

        //上传图片的url
        String uploadUrl = null;
        //图片文件
        MultipartFile image = vo.getDisplayImage();

        //step1 -> 上传图片到 oss
        if(image != null){
            if(image.getSize() > Constant.IMAGE_MAX_SIZE){
                throw new IllegalArgumentException("图片超出上传大小限制");
            }

            ResultObject<String> r = ossFeignService.uploadImage(image);
            if(!r.getResponseCode().equals(200)){
                throw new ThirdPartyServiceException("third_party微服务远程调用异常");
            }

            uploadUrl = r.getResult();
        }

        //step2 -> 修改商品信息表
        ProductInfo todoUpdate = new ProductInfo();
        BeanUtil.copyProperties(vo, todoUpdate);
        //设置新商品展示图片
        todoUpdate.setDisplayImage(uploadUrl);
        this.baseMapper.updateById(todoUpdate);

        //step3 -> 删除 oss 原商品展示图片
        if(uploadUrl != null){
            ossFeignService.deleteFile(productInfo.getDisplayImage());
        }
    }

    /**
     * 上架商品
     * @param productId 商品id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upProduct(String productId) {
        String uid = StpUtil.getLoginId().toString();
        //step1 -> 获取商品信息
        ProductInfoVo productInfo = this.baseMapper.getUserSelfProductInfoById(productId, uid);
        if(productInfo == null){
            throw new NoSuchResultException("无此productId商品信息");
        }

        //step2 -> 修改商品信息状态为上架状态
        ProductInfo todoUpdate = new ProductInfo();
        //上架时间
        LocalDateTime now = LocalDateTime.now();
        todoUpdate.setProductId(productId)
                .setStatus(ProductStatus.UP)
                .setUpTime(now);
        //修改商品信息表
        this.baseMapper.updateById(todoUpdate);

        //step3 -> 远程调用用户微服务获取用户信息
        ResultObject<MemberInfoTo> r = memberInfoFeignService.userSelfInfo(StpUtil.getTokenValue());
        if(!r.getResponseCode().equals(200)){
            throw new MemberServiceException("member微服务远程调用异常，code: " + r.getResponseCode());
        }

        //获取远程调用用户信息结果
        MemberInfoTo memberInfoTo = r.getResult();

        //step4 -> 向 elasticsearch 添加商品信息
        ProductInfoES productInfoES = new ProductInfoES();
        //将商品信息复制到 elasticsearch 实体类
        BeanUtil.copyProperties(productInfo, productInfoES);
        productInfoES.setUserName(memberInfoTo.getUserName())
                .setRealName(memberInfoTo.getRealName())
                .setUpTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        //elasticsearch 添加商品信息
        productInfoESService.newProductInfoToES(productInfoES);

        log.info("商品上架成功，productId: " + productId);
    }
}
