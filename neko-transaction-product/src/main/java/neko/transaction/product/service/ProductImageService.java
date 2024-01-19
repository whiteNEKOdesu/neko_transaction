package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.entity.ProductImage;

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
    List<ProductImage> allById(String productId);
}
