package neko.transaction.product.feign.ware;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.product.to.LockStockTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 库存微服务库存信息远程调用
 */
@FeignClient(value = ServiceName.WARE_SERVICE, contextId = "WareInfo")
public interface WareInfoFeignService {
    /**
     * 添加库存信息
     * @param productId 商品id
     * @param stock 库存数量
     * @return 响应结果
     */
    @PostMapping("ware_info/new_ware_info")
    ResultObject<Object> newWareInfo(@RequestParam String productId, @RequestParam Integer stock);

    /**
     * 锁定指定库存数量
     * @param to 锁定库存to
     * @return 响应结果
     */
    @PostMapping("ware_info/lock_stock")
    ResultObject<Object> lockStock(@Validated @RequestBody LockStockTo to);

    /**
     * 解锁指定订单号库存
     */
    @PostMapping("ware_info/unlock_stock")
    ResultObject<Object> unlockStock(@RequestParam String orderId);
}
