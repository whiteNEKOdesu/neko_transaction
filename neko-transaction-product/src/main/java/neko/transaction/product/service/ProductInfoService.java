package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;

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
}
