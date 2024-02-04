package neko.transaction.member.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import neko.transaction.member.entity.MemberChatInfo;
import neko.transaction.member.mapper.MemberChatInfoMapper;
import neko.transaction.member.service.MemberChatInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.vo.NewMemberChatVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天消息信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
@Service
public class MemberChatInfoServiceImpl extends ServiceImpl<MemberChatInfoMapper, MemberChatInfo> implements MemberChatInfoService {

    /**
     * 添加聊天消息
     * @param vo 添加聊天消息的vo
     */
    @Override
    public void newMemberChatInfo(NewMemberChatVo vo) {
        String uid = StpUtil.getLoginId().toString();
        MemberChatInfo memberChatInfo = new MemberChatInfo();
        memberChatInfo.setFromId(uid)
                .setToId(vo.getToId())
                .setBody(vo.getBody());

        this.baseMapper.insert(memberChatInfo);
    }
}
