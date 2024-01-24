package neko.transaction.product.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductImageMapper;
import neko.transaction.product.service.ProductImageService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.vo.NewProductImageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品图片信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private OSSFeignService ossFeignService;

    /**
     * 查询所有指定的商品id对应的商品图片信息
     * @param productId 商品id
     * @return 响应结果
     */
    @Override
    public List<ProductImage> allByProductId(String productId) {
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        //匹配商品id
        queryWrapper.lambda().eq(ProductImage::getProductId, productId);

        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询指定的商品id对应的商品图片信息
     * @param vo 查询vo
     * @return 响应结果
     */
    @Override
    public Page<ProductImage> pageQueryByProductId(QueryVo vo) {
        Object objectId = vo.getObjectId();
        if(objectId == null){
            throw new IllegalArgumentException("缺少参数商品id");
        }

        Page<ProductImage> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        //匹配商品id
        queryWrapper.lambda().eq(ProductImage::getProductId, objectId.toString());

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 添加商品图片
     * @param vo 添加商品图片vo
     */
    @Override
    public void newProductImage(NewProductImageVo vo) {
        String productId = vo.getProductId();
        ProductInfo productInfo = productInfoService.getById(productId);

        if(productInfo == null){
            throw new IllegalArgumentException("productId对应商品信息不存在");
        }
        //商品不属于该用户
        if(!productInfo.getUid().equals(StpUtil.getLoginId())){
            throw new NotPermissionException("访问商品信息权限不足");
        }

        //获取已上传的图片数量
        Long count = this.baseMapper.selectCount(new QueryWrapper<ProductImage>().lambda().eq(ProductImage::getProductId, productId));
        if(count != null && count > Constant.MAX_IMAGE_NUM_PER_PRODUCT){
            throw new IllegalArgumentException("超出商品最大上传图片数量");
        }

        //上传图片的url
        String uploadUrl = null;
        //图片文件
        MultipartFile image = vo.getProductImage();

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

        //step2 -> 向商品图片表添加数据
        ProductImage productImage = new ProductImage();
        productImage.setProductId(productId)
                .setProductImage(uploadUrl);

        this.baseMapper.insert(productImage);
    }
}
