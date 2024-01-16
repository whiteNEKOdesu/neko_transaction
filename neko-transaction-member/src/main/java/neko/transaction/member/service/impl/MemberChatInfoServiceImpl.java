package neko.transaction.member.service.impl;

import neko.transaction.member.entity.MemberChatInfo;
import neko.transaction.member.mapper.MemberChatInfoMapper;
import neko.transaction.member.service.MemberChatInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
