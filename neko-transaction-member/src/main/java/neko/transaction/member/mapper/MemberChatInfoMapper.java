package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberChatInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.member.vo.MemberChatInfoLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    /**
     * 分页查询学生自身的聊天对象信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param uid 学号
     * @return 查询结果
     */
    List<MemberChatInfoLogVo> memberChattingWithPageQuery(Integer limited,
                                                Integer start,
                                                String uid);

    /**
     * 分页查询学生自身的聊天对象信息的结果总数量
     * @param uid 学号
     * @return 查询结果的结果总数量
     */
    int memberChattingWithPageQueryNumber(String uid);
}
