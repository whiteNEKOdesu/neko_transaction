package neko.transaction.member.service;

import neko.transaction.member.entity.AdminLogInLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员登录记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface AdminLogInLogService extends IService<AdminLogInLog> {
    /**
     * 添加管理员登录记录
     * @param uid 用户id
     * @param ip 登录ip
     * @param isLogIn 是否登录成功
     */
    void newLog(String uid, String ip, Boolean isLogIn);
}
