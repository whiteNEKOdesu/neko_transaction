package neko.transaction.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.entity.MemberChatInfoReadLog;
import neko.transaction.member.mapper.MemberChatInfoReadLogMapper;
import neko.transaction.member.service.MemberChatInfoReadLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 聊天消息已读记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-02-05
 */
@Service
public class MemberChatInfoReadLogServiceImpl extends ServiceImpl<MemberChatInfoReadLogMapper, MemberChatInfoReadLog> implements MemberChatInfoReadLogService {

    /**
     * 添加已读消息记录
     * @param chatIds 聊天id集合
     * @param fromId 发送人学号
     * @param toId 接收人学号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void newMemberChatInfoReadLog(List<Long> chatIds, String fromId, String toId) {
        if(chatIds == null || chatIds.isEmpty()){
            return;
        }

        this.baseMapper.insertBatch(chatIds, fromId, toId);
    }
}
