package neko.transaction.member.service.impl;

import neko.transaction.member.entity.AdminLogInLog;
import neko.transaction.member.mapper.AdminLogInLogMapper;
import neko.transaction.member.service.AdminLogInLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 管理员登录记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class AdminLogInLogServiceImpl extends ServiceImpl<AdminLogInLogMapper, AdminLogInLog> implements AdminLogInLogService {

    /**
     * 添加管理员登录记录
     * @param uid 用户id
     * @param ip 登录ip
     * @param isLogIn 是否登录成功
     */
    @Override
    public void newLog(String uid, String ip, Boolean isLogIn) {
        AdminLogInLog adminLogInLog = new AdminLogInLog();
        adminLogInLog.setAdminId(uid)
                .setIp(ip)
                .setIsLogIn(isLogIn)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        this.baseMapper.insert(adminLogInLog);
    }
}
