package neko.transaction.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.vo.MemberInfoVo;
import neko.transaction.member.vo.MemberWithSchoolInfoVo;
import neko.transaction.member.vo.PublicMemberInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 学生信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface MemberInfoMapper extends BaseMapper<MemberInfo> {
    /**
     * 分页查询学生及所属二级学院，专业，班级信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @return 查询结果
     */
    List<MemberWithSchoolInfoVo> memberWithSchoolInfoPageQuery(Integer limited,
                                                               Integer start,
                                                               String queryWords,
                                                               String classId);

    /**
     * 获取分页查询查询学生及所属二级学院，专业，班级信息的结果总页数
     * @param queryWords 查询条件
     * @return 查询结果页数
     */
    int memberWithSchoolInfoPageQueryNumber(String queryWords,
                                            String classId);

    /**
     * 根据学号获取用户信息
     * @param uid 学号
     * @return 用户信息
     */
    MemberInfoVo getMemberInfoByUid(String uid);

    /**
     * 根据学号获取用户公开信息
     * @param uid 学号
     * @return 用户公开信息
     */
    PublicMemberInfoVo getPublicMemberInfoByUid(String uid);
}
