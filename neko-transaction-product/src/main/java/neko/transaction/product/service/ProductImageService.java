package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.vo.NewProductImageVo;

import java.util.List;

/**
 * <p>
 * 商品图片信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
public interface ProductImageService extends IService<ProductImage> {
    /**
     * 查询所有指定的商品id对应的商品图片信息
     * @param productId 商品id
     * @return 响应结果
     */
    List<ProductImage> allByProductId(String productId);

    /**
     * 分页查询指定的商品id对应的商品图片信息
     * @param vo 查询vo
     * @return 响应结果
     */
    Page<ProductImage> pageQueryByProductId(QueryVo vo);

    /**
     * 添加商品图片
     * @param vo 添加商品图片vo
     */
    void newProductImage(NewProductImageVo vo);
}
