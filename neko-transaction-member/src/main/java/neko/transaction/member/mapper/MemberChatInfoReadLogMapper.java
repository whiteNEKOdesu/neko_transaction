package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberChatInfoReadLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    /**
     * 批量添加已读消息记录
     * @param chatIds 聊天id集合
     * @param fromId 发送人id
     * @param toId 接收人id
     */
    void insertBatch(List<Long> chatIds, String fromId, String toId);
}
