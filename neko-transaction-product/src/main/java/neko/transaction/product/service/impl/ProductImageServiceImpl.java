package neko.transaction.product.service.impl;

import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.mapper.ProductImageMapper;
import neko.transaction.product.service.ProductImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
