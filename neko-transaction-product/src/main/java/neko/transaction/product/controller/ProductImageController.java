package neko.transaction.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.entity.ProductImage;
import neko.transaction.product.service.ProductImageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品图片信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@RestController
@RequestMapping("product_image")
public class ProductImageController {
    @Resource
    private ProductImageService productImageService;

    /**
     * 查询所有指定的商品id对应的商品图片信息
     * @param productId 商品id
     * @return 响应结果
     */
    @GetMapping("page_query_by_id")
    public ResultObject<List<ProductImage>> pageQueryById(@RequestParam String productId){
        return ResultObject.ok(productImageService.allById(productId));
    }
}
