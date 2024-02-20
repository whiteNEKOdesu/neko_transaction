package neko.transaction.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.entity.ProductComment;
import neko.transaction.product.service.ProductCommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 商品评论表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-02-20
 */
@RestController
@RequestMapping("product_comment")
public class ProductCommentController {
    @Resource
    private ProductCommentService productCommentService;

    /**
     * 分页查询商品评论信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @PostMapping("product_comment_page_query")
    public ResultObject<Page<ProductComment>> productCommentPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(productCommentService.productCommentPageQuery(vo));
    }
}
