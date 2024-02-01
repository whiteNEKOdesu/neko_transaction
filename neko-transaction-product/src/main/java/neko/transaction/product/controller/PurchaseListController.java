package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.PurchaseListService;
import neko.transaction.product.vo.AddToPurchaseListVo;
import neko.transaction.product.vo.PurchaseListRedisTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@RestController
@RequestMapping("purchase_list")
public class PurchaseListController {
    @Resource
    private PurchaseListService purchaseListService;

    /**
     * 添加商品到购物车
     * @param vo 添加商品到购物车的vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PutMapping("add_to_purchase_list")
    public ResultObject<Object> addToPurchaseList(@Validated @RequestBody AddToPurchaseListVo vo){
        purchaseListService.addToPurchaseList(vo);

        return ResultObject.ok();
    }

    /**
     * 获取用户自身的购物车全部商品信息
     * @return 用户自身的购物车全部商品信息
     */
    @SaCheckLogin
    @GetMapping("purchase_list_infos")
    public ResultObject<List<PurchaseListRedisTo>> purchaseListInfos(){
        return ResultObject.ok(purchaseListService.userSelfPurchaseListInfos());
    }
}
