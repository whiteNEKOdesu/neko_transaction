package neko.transaction.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.entity.MemberLogInLog;
import neko.transaction.member.mapper.MemberLogInLogMapper;
import neko.transaction.member.service.MemberLogInLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class MemberLogInLogServiceImpl extends ServiceImpl<MemberLogInLogMapper, MemberLogInLog> implements MemberLogInLogService {

    /**
     * 添加用户登录记录
     * @param uid 用户id
     * @param ip 登录ip
     * @param isLogIn 是否登录成功
     */
    @Override
    public void newLog(String uid, String ip, Boolean isLogIn) {
        MemberLogInLog memberLogInLog = new MemberLogInLog();
        memberLogInLog.setUid(uid)
                .setIp(ip)
                .setIsLogIn(isLogIn);

        this.baseMapper.insert(memberLogInLog);
    }
}
