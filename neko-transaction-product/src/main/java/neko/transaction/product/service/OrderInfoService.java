package neko.transaction.product.service;

import com.alipay.api.AlipayApiException;
import neko.transaction.product.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.AliPayAsyncVo;
import neko.transaction.product.vo.NewOrderInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 生成 token 保证创建订单接口幂等性，用于创建订单传入
     * @return 创建订单传入的 token
     */
    String getPreOrderToken();

    /**
     * 添加订单
     * @param vo 提交订单vo
     * @return 订单号
     */
    String newOrder(NewOrderInfoVo vo) throws ExecutionException, InterruptedException;

    /**
     * 根据订单号获取支付宝支付页面
     * @param orderId 订单号
     * @param token 用户登录认证的 token
     * @return 支付宝支付页面
     */
    String getAlipayPage(String orderId, String token);

    /**
     * 根据订单号获取订单信息，用于检查订单状态
     * @param orderId 订单号
     * @return 订单信息
     */
    OrderInfo getOrderInfoById(String orderId);

    /**
     * 根据订单号将订单状态修改为取消状态
     * @param orderId 订单号
     */
    void updateOrderInfoStatusToCancel(String orderId);

    /**
     * 支付宝支付成功异步通知处理
     * @param vo 支付宝支付成功异步通知vo
     * @param request HttpServletRequest
     * @return 向支付宝响应的处理结果
     */
    String alipayTradeCheck(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException;
}
