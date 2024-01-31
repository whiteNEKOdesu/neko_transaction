package neko.transaction.ware.feign.product;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.ware.to.OrderInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品微服务订单信息远程调用
 */
@FeignClient(value = ServiceName.PRODUCT_SERVICE, contextId = "OrderInfo")
public interface OrderInfoFeignService {
    /**
     * 根据订单号获取订单信息，用于检查订单状态，建议只提供给微服务远程调用
     * @param orderId 订单号
     * @return 订单信息
     */
    @GetMapping("order_info/remote_invoke_order_info_by_id")
    ResultObject<OrderInfoTo> remoteInvokeOrderInfoById(@RequestParam String orderId);
}
