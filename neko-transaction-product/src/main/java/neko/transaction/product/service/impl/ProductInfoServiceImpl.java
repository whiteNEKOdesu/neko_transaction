package neko.transaction.product.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.service.ProductInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.ProductApplyInfoVo;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private OSSFeignService ossFeignService;

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
}
