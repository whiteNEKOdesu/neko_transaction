package neko.transaction.member.service;

import neko.transaction.member.entity.MemberChatInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.NewMemberChatVo;

/**
 * <p>
 * 聊天消息信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
public interface MemberChatInfoService extends IService<MemberChatInfo> {
    /**
     * 添加聊天消息
     * @param vo 添加聊天消息的vo
     */
    void newMemberChatInfo(NewMemberChatVo vo);
}
