package neko.transaction.member.service.impl;

import neko.transaction.member.entity.MqReturnMessage;
import neko.transaction.member.mapper.MqReturnMessageMapper;
import neko.transaction.member.service.MqReturnMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * rabbitmq消息发送失败记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class MqReturnMessageServiceImpl extends ServiceImpl<MqReturnMessageMapper, MqReturnMessage> implements MqReturnMessageService {

}
