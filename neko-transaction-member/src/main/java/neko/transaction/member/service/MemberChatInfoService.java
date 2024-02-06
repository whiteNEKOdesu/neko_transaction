package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.MemberChatInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.MemberChatInfoLogVo;
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

    /**
     * 分页查询指定的聊天对象学号的聊天信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<MemberChatInfo> pageQueryByToId(QueryVo vo);

    /**
     * 分页查询用户自身的聊天对象信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<MemberChatInfoLogVo> memberChattingWithPageQuery(QueryVo vo);
}
