package neko.transaction.ware.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.ware.service.WareInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 库存信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@RestController
@RequestMapping("ware_info")
public class WareInfoController {
    @Resource
    private WareInfoService wareInfoService;

    /**
     * 根据商品id获取库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    @GetMapping("ware_info_by_id")
    public ResultObject<Object> wareInfoById(@RequestParam String productId){
        return ResultObject.ok(wareInfoService.wareInfoById(productId));
    }

    /**
     * 添加库存信息，仅提供给微服务远程调用
     * @param productId 商品id
     * @param stock 库存数量
     * @return 响应结果
     */
    @PostMapping("new_ware_info")
    public ResultObject<Object> newWareInfo(@RequestParam String productId, @RequestParam Integer stock){
        wareInfoService.newWareInfo(productId, stock);

        return ResultObject.ok();
    }

    /**
     * 修改库存数量
     * @param productId 商品id
     * @param offset 库存偏移量
     */
    @SaCheckLogin
    @PostMapping("update_stock")
    public ResultObject<Object> updateStock(@RequestParam String productId, @RequestParam Integer offset){
        wareInfoService.updateStock(productId, offset);

        return ResultObject.ok();
    }
}
