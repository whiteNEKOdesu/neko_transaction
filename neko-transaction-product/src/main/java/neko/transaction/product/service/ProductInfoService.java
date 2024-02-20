package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewProductCommentVo;
import neko.transaction.product.vo.ProductDetailInfoVo;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
public interface ProductInfoService extends IService<ProductInfo> {
    /**
     * 分页查询学生自身的商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    Page<ProductInfoVo> userSelfPageQuery(QueryVo vo);

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    ProductInfoVo getUserSelfProductInfoById(String productId);

    /**
     * 修改商品信息
     * @param vo 修改商品信息vo
     */
    void updateProductInfo(UpdateProductInfoVo vo);

    /**
     * 上架商品
     * @param productId 商品id
     */
    void upProduct(String productId);

    /**
     * 下架商品
     * @param productId 商品id
     */
    void downProduct(String productId);

    /**
     * 获取上架的商品详情信息
     * @param productId 商品id
     * @return 商品详情信息
     */
    ProductDetailInfoVo getUpProductDetailInfo(String productId);

    /**
     * 根据商品id集合获取商品详情信息
     * @param productIds 商品id集合
     * @return 商品id集合对应的商品详情信息
     */
    List<ProductDetailInfoVo> getProductDetailInfoByIds(List<String> productIds);

    /**
     * 添加销量
     * @param productId 商品id
     * @param increase 要添加的数量
     */
    void increaseSaleNumber(String productId, Integer increase);

    /**
     * 添加商品评论
     * @param vo 添加商品评论vo
     */
    void newProductComment(NewProductCommentVo vo);
}
