package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberChatInfoReadLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 聊天消息已读记录表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-02-05
 */
@Mapper
public interface MemberChatInfoReadLogMapper extends BaseMapper<MemberChatInfoReadLog> {

}
