package neko.transaction.ware.feign.product;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.ware.to.ProductInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品微服务商品信息远程调用
 */
@FeignClient(value = ServiceName.PRODUCT_SERVICE, contextId = "ProductInfo")
public interface ProductInfoFeignService {
    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    @GetMapping("product_info/user_self_product_by_id")
    ResultObject<ProductInfoTo> userSelfProductById(@RequestParam String productId, @RequestHeader("neko_transaction") String token);
}
