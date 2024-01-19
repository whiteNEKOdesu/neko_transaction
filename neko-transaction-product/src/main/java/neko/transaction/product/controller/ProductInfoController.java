package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.vo.ProductInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@RestController
@RequestMapping("product_info")
public class ProductInfoController {
    @Resource
    private ProductInfoService productInfoService;

    /**
     * 分页查询学生自身的商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @SaCheckLogin
    @PostMapping("user_self_page_query")
    public ResultObject<Page<ProductInfoVo>> userSelfPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(productInfoService.userSelfPageQuery(vo));
    }
}
