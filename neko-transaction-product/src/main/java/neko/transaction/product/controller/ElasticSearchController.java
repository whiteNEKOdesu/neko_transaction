package neko.transaction.product.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.elasticsearch.service.ProductInfoESService;
import neko.transaction.product.vo.ProductInfoESQueryVo;
import neko.transaction.product.vo.ProductInfoESVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 商品信息 elasticsearch 控制器
 */
@RestController
@RequestMapping("elasticsearch")
public class ElasticSearchController {
    @Resource
    private ProductInfoESService productInfoESService;

    /**
     * elasticsearch分页查询查询商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @PostMapping("product_infos")
    public ResultObject<ProductInfoESVo> productInfos(@Validated @RequestBody ProductInfoESQueryVo vo) throws IOException {
        return ResultObject.ok(productInfoESService.productInfoPageQuery(vo));
    }
}
