package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
