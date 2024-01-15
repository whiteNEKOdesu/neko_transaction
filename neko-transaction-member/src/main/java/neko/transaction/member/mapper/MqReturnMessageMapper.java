package neko.transaction.member.mapper;

import neko.transaction.member.entity.MqReturnMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * rabbitmq消息发送失败记录表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface MqReturnMessageMapper extends BaseMapper<MqReturnMessage> {

}
