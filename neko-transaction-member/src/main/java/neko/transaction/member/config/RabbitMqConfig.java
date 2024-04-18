package neko.transaction.member.config;

import neko.transaction.commonbase.utils.entity.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 使用JSON序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    //---------------------------------------------------------------------------------------------------
    /**
     * 聊天消息发布队列配置
     * <br/>
     * ---------------------------------------------------------------------------------------------------
     * <br/>
     * <br/>
     * <br/>
     * 聊天消息发布交换机
     */
    @Bean
    public FanoutExchange chatPubExchange(){
        return ExchangeBuilder.fanoutExchange(RabbitMqConstant.CHAT_PUB_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 聊天消息发布队列
     */
    @Bean
    public Queue chatPubQueue(){
        return QueueBuilder.durable(RabbitMqConstant.CHAT_PUB_QUEUE_NAME)
                .build();
    }

    /**
     * 聊天消息发布队列跟聊天消息发布交换机绑定
     */
    @Bean
    public Binding orderHandleBinding(Queue chatPubQueue, FanoutExchange chatPubExchange){
        return BindingBuilder.bind(chatPubQueue)
                .to(chatPubExchange);
    }
}
