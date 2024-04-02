package neko.transaction.product.service;

import neko.transaction.product.entity.MqReturnMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.to.RabbitMQMessageTo;

import java.util.List;

/**
 * <p>
 * rabbitmq消息发送失败记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-02
 */
public interface MqReturnMessageService extends IService<MqReturnMessage> {
    /**
     * 记录发送失败回退消息
     * @param to rabbitmq 消息to
     * @param jsonMessage json消息
     */
    void logReturnMessage(RabbitMQMessageTo<Object> to, String jsonMessage);

    /**
     * 根据 rabbitmq回退消息id 批量删除发送失败记录
     * @param mqReturnIds rabbitmq回退消息id List
     */
    void deleteMqReturnMessageByMqReturnIds(List<Long> mqReturnIds);
}
