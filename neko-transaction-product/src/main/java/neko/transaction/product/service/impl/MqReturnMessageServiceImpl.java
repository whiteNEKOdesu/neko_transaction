package neko.transaction.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.entity.MqReturnMessage;
import neko.transaction.product.mapper.MqReturnMessageMapper;
import neko.transaction.product.service.MqReturnMessageService;
import neko.transaction.product.to.RabbitMQMessageTo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * rabbitmq消息发送失败记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-02
 */
@Service
public class MqReturnMessageServiceImpl extends ServiceImpl<MqReturnMessageMapper, MqReturnMessage> implements MqReturnMessageService {

    /**
     * 记录发送失败回退消息
     * @param to rabbitmq 消息to
     * @param jsonMessage json消息
     */
    @Override
    public void logReturnMessage(RabbitMQMessageTo<Object> to, String jsonMessage) {
        MqReturnMessage mqReturnMessage = new MqReturnMessage();
        mqReturnMessage.setMessage(jsonMessage)
                .setType(to.getType());

        this.baseMapper.insert(mqReturnMessage);
    }

    /**
     * 根据 rabbitmq回退消息id 批量删除发送失败记录
     * @param mqReturnIds rabbitmq回退消息id List
     */
    @Override
    public void deleteMqReturnMessageByMqReturnIds(List<Long> mqReturnIds) {
        this.baseMapper.deleteMqReturnMessageByMqReturnIds(mqReturnIds);
    }
}
