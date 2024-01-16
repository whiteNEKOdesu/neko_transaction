package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberChatInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 聊天消息信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
@Mapper
public interface MemberChatInfoMapper extends BaseMapper<MemberChatInfo> {

}
