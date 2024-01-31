package neko.transaction.product.listener;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.OrderStatus;
import neko.transaction.commonbase.utils.entity.RabbitMqConstant;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.RabbitMQMessageRejectException;
import neko.transaction.commonbase.utils.exception.WareServiceException;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.feign.ware.WareInfoFeignService;
import neko.transaction.product.service.OrderInfoService;
import neko.transaction.product.to.RabbitMQMessageTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * rabbitmq订单处理延迟队列监听
 */
@Component
@RabbitListener(queues = RabbitMqConstant.ORDER_HANDLE_QUEUE_NAME)
@Slf4j
public class OrderListener {
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    /**
     * 监听 rabbitmq 订单处理队列
     */
    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void stockUnlock(String jsonMessage, Message message, Channel channel) throws IOException {
        RabbitMQMessageTo<String> rabbitMQMessageTo = JSONUtil.toBean(jsonMessage,
                new TypeReference<RabbitMQMessageTo<String>>() {},
                true);
        //获取订单号
        String orderId = rabbitMQMessageTo.getMessage();

        try {
            //获取订单信息
            OrderInfo orderInfo = orderInfoService.getById(orderId);
            if(orderInfo == null){
                log.warn("超时订单号: " + orderId + "，订单不存在，尝试关闭订单");
                //解锁库存
                unlockStock(orderId);
                //单个确认消费消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                return;
            }

            //订单不为未支付状态，无需取消订单
            if(!orderInfo.getStatus().equals(OrderStatus.UNPAID)){
                //单个确认消费消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                return;
            }

            log.info("订单超时准备验证关闭，订单号: " + orderId);
            //修改订单状态为取消状态
            orderInfoService.updateOrderInfoStatusToCancel(orderId);

            //解锁库存
            unlockStock(orderId);

            //单个确认消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            e.printStackTrace();
            log.error("订单关闭发生异常，订单号: " + orderId);
            //拒收消息，并让消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            throw new RabbitMQMessageRejectException("订单关闭发生异常");
        }

        log.info("订单超时关闭完成，订单号: " + orderId);
    }

    /**
     * 根据订单号结果库存
     * @param orderId 订单号
     */
    private void unlockStock(String orderId){
        //远程调用库存微服务解锁库存
        ResultObject<Object> r = wareInfoFeignService.unlockStock(orderId);
        if(!r.getResponseCode().equals(200)){
            throw new WareServiceException("ware微服务远程调用异常");
        }
    }
}
