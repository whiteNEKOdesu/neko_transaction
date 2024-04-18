package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.vo.NewProductCommentVo;
import neko.transaction.product.vo.ProductDetailInfoVo;
import neko.transaction.product.vo.ProductInfoVo;
import neko.transaction.product.vo.UpdateProductInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    @SaCheckLogin
    @GetMapping("user_self_product_by_id")
    public ResultObject<ProductInfoVo> userSelfProductById(@RequestParam String productId){
        return ResultObject.ok(productInfoService.getUserSelfProductInfoById(productId));
    }

    /**
     * 修改商品信息
     * @param vo 修改商品信息vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("update_product_info")
    public ResultObject<Object> updateProductInfo(@Validated UpdateProductInfoVo vo){
        productInfoService.updateProductInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 上架商品
     * @param productId 商品id
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("up_product")
    public ResultObject<Object> upProduct(@RequestParam String productId){
        productInfoService.upProduct(productId);

        return ResultObject.ok();
    }

    /**
     * 下架商品
     * @param productId 商品id
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("down_product")
    public ResultObject<Object> downProduct(@RequestParam String productId){
        productInfoService.downProduct(productId);

        return ResultObject.ok();
    }

    /**
     * 获取上架的商品详情信息
     * @param productId 商品id
     * @return 商品详情信息
     */
    @GetMapping("product_detail_info")
    public ResultObject<ProductDetailInfoVo> productDetailInfo(@RequestParam String productId){
        return ResultObject.ok(productInfoService.getUpProductDetailInfo(productId));
    }

    /**
     * 添加商品评论
     * @param vo 添加商品评论vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PutMapping("new_comment")
    public ResultObject<Object> newComment(@Validated @RequestBody NewProductCommentVo vo){
        productInfoService.newProductComment(vo);

        return ResultObject.ok();
    }

    /**
     * 获取销量前 8 的商品信息
     * @return 销量前 8 的商品信息
     */
    @GetMapping("top_8_product_infos")
    public ResultObject<List<ProductInfo>> top8ProductInfos(){
        return ResultObject.ok(productInfoService.getTop8SaleNumberProductInfos());
    }

    /**
     * 将商品信息同步到 elasticsearch
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("synchronize_product_info_to_es")
    public ResultObject<Object> synchronizeProductInfoToES(){
        productInfoService.synchronizeProductInfoToES();

        return ResultObject.ok();
    }
}
