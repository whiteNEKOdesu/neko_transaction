package neko.transaction.ware.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.ware.service.WareInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
