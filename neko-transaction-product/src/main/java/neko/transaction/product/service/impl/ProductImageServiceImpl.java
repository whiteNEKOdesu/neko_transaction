package neko.transaction.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.mapper.ProductImageMapper;
import neko.transaction.product.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
}
