package neko.transaction.member.mapper;

import neko.transaction.member.entity.AdminLogInLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 管理员登录记录表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface AdminLogInLogMapper extends BaseMapper<AdminLogInLog> {

}
