package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.service.OrderInfoService;
import neko.transaction.product.vo.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 支付宝支付成功异步通知处理
     * @param vo 支付宝支付成功异步通知vo
     * @param request HttpServletRequest
     * @return 向支付宝响应的处理结果
     */
    @PostMapping("alipay_listener")
    public String alipayListener(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        return orderInfoService.alipayTradeCheck(vo, request);
    }

    /**
     * 根据订单号获取用户自身的订单信息
     * @param orderId 订单号
     * @return 用户自身的订单信息
     */
    @SaCheckLogin
    @GetMapping("user_self_order_info_by_order_id")
    public ResultObject<OrderInfo> userSelfOrderInfoByOrderId(@RequestParam String orderId){
        return ResultObject.ok(orderInfoService.getUserSelfOrderInfoByOrderId(orderId));
    }

    /**
     * 分页查询学生自身的订单信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckLogin
    @PostMapping("user_self_page_query")
    public ResultObject<Page<OrderInfoPageQueryVo>> userSelfPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(orderInfoService.userSelfPageQuery(vo));
    }

    /**
     * 获取订单按照状态聚合信息
     * @return 订单按照状态聚合信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @GetMapping("status_agg")
    public ResultObject<List<OrderStatusAggCountVo>> statusAgg(){
        return ResultObject.ok(orderInfoService.statusAggCount());
    }

    /**
     * 获取指定年的订单流水信息
     * @param year 指定年
     * @return 订单流水信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @GetMapping("transaction_in_year")
    public ResultObject<List<OrderTransactionInYearVo>> transactionInYear(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                              @RequestParam
                                                                              LocalDateTime year){
        return ResultObject.ok(orderInfoService.transactionInYear(year));
    }
}
