package neko.transaction.product.elasticsearch.service;

import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.vo.ProductInfoESQueryVo;
import neko.transaction.product.vo.ProductInfoESVo;

import java.io.IOException;
import java.util.List;

/**
 * 商品信息 elasticsearch 查询服务类
 */
public interface ProductInfoESService {
    /**
     * 商品信息 elasticsearch 查询
     * @param vo 查询vo
     * @return 查询结果
     */
    ProductInfoESVo productInfoPageQuery(ProductInfoESQueryVo vo) throws IOException;

    /**
     * 添加商品信息到 elasticsearch中
     * @param productInfoES 商品es信息
     */
    void newProductInfoToES(ProductInfoES productInfoES);

    /**
     * 批量添加商品信息到 elasticsearch中
     * @param productInfoESs 商品es信息 List
     */
    void newProductInfosToES(List<ProductInfoES> productInfoESs);

    /**
     * 根据商品id删除 elasticsearch 数据
     * @param productId 商品id
     */
    void deleteByProductId(String productId);

    /**
     * 更新 elasticsearch 商品信息
     * @param productInfoES elasticsearch商品信息实体类
     */
    void updateProductInfo(ProductInfoES productInfoES);
}
