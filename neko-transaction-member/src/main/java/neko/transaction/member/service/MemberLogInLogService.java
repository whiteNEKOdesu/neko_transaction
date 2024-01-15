package neko.transaction.member.service;

import neko.transaction.member.entity.MemberLogInLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface MemberLogInLogService extends IService<MemberLogInLog> {
    /**
     * 添加用户登录记录
     * @param uid 用户id
     * @param ip 登录ip
     * @param isLogIn 是否登录成功
     */
    void newLog(String uid, String ip, Boolean isLogIn);
}
