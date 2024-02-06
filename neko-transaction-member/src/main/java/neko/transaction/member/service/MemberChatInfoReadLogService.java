package neko.transaction.member.service;

import neko.transaction.member.entity.MemberChatInfoReadLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 聊天消息已读记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-02-05
 */
public interface MemberChatInfoReadLogService extends IService<MemberChatInfoReadLog> {
    /**
     * 添加已读消息记录
     * @param chatIds 聊天id集合
     * @param fromId 发送人学号
     * @param toId 接收人学号
     */
    void newMemberChatInfoReadLog(List<Long> chatIds, String fromId, String toId);
}
