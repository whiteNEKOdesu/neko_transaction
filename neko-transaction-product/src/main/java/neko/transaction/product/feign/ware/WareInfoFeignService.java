package neko.transaction.product.feign.ware;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 库存微服务库存信息远程调用
 */
@FeignClient(value = ServiceName.WARE_SERVICE, contextId = "WareInfo")
public interface WareInfoFeignService {
    /**
     * 添加库存信息，仅提供给微服务远程调用
     * @param productId 商品id
     * @param stock 库存数量
     * @return 响应结果
     */
    @PostMapping("ware_info/new_ware_info")
    ResultObject<Object> newWareInfo(@RequestParam String productId, @RequestParam Integer stock);
}
