package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.service.OrderInfoService;
import neko.transaction.product.vo.NewOrderInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@RestController
@RequestMapping("order_info")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 生成 token 保证创建订单接口幂等性，用于创建订单传入
     * @return 创建订单传入的 token
     */
    @SaCheckLogin
    @GetMapping("preorder_token")
    public ResultObject<String> preorderToken(){
        return ResultObject.ok(orderInfoService.getPreOrderToken());
    }

    /**
     * 提交订单
     * @param vo 提交订单vo
     * @return 订单号
     */
    @SaCheckLogin
    @PutMapping("new_order")
    public ResultObject<String> newOrder(@Validated @RequestBody NewOrderInfoVo vo) throws ExecutionException, InterruptedException {
        return ResultObject.ok(orderInfoService.newOrder(vo));
    }

    /**
     * 根据订单号获取支付宝支付页面
     */
    @GetMapping(value = "alipay_page", produces = "text/html")
    public String alipayPage(@RequestParam String orderId, @RequestParam String token){
        return orderInfoService.getAlipayPage(orderId, token);
    }

    /**
     * 根据订单号获取订单信息，用于检查订单状态，建议只提供给微服务远程调用
     * @param orderId 订单号
     * @return 订单信息
     */
    @GetMapping("remote_invoke_order_info_by_id")
    public ResultObject<OrderInfo> remoteInvokeOrderInfoById(@RequestParam String orderId){
        return ResultObject.ok(orderInfoService.getOrderInfoById(orderId));
    }
}
