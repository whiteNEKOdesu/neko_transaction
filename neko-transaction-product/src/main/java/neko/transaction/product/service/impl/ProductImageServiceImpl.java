package neko.transaction.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.mapper.ProductImageMapper;
import neko.transaction.product.service.ProductImageService;
import org.springframework.stereotype.Service;

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

    /**
     * 查询所有指定的商品id对应的商品图片信息
     * @param productId 商品id
     * @return 响应结果
     */
    @Override
    public List<ProductImage> allById(String productId) {
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        //匹配商品id
        queryWrapper.lambda().eq(ProductImage::getProductId, productId);

        return this.baseMapper.selectList(queryWrapper);
    }
}
