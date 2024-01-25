package neko.transaction.product.elasticsearch.service;

import neko.transaction.product.elasticsearch.entity.ProductInfoES;
import neko.transaction.product.vo.ProductInfoESQueryVo;
import neko.transaction.product.vo.ProductInfoESVo;

import java.io.IOException;

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
     */
    void newProductInfoToES(ProductInfoES productInfoES);
}
