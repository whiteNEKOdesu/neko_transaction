package neko.transaction.member.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.Response;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.AdminInfo;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.ip.IPHandler;
import neko.transaction.member.mapper.AdminInfoMapper;
import neko.transaction.member.service.AdminInfoService;
import neko.transaction.member.service.AdminLogInLogService;
import neko.transaction.member.service.WeightRoleRelationService;
import neko.transaction.member.vo.AdminInfoVo;
import neko.transaction.member.vo.LogInVo;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {
    @Resource
    private AdminLogInLogService adminLogInLogService;

    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Resource
    private RSA rsa;

    /**
     * 管理员登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 管理员信息vo
     */
    @Override
    public ResultObject<AdminInfoVo> login(LogInVo vo, HttpServletRequest request) {
        ResultObject<AdminInfoVo> resultObject = new ResultObject<>();
        //根据用户名查询用户信息
        AdminInfo adminInfo = this.baseMapper.selectOne(new QueryWrapper<AdminInfo>().lambda()
                .eq(AdminInfo::getUserName, vo.getUserName()));

        if(adminInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            //RSA 解密获取密码
            String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            //校验 MD5 hash 后的密码
            if(DigestUtils.md5DigestAsHex((userPassword + adminInfo.getSalt()).getBytes()).equals(adminInfo.getUserPassword())){
                StpUtil.login(adminInfo.getAdminId());
                AdminInfoVo adminInfoVo = new AdminInfoVo();
                BeanUtil.copyProperties(adminInfo, adminInfoVo);
                //设置token
                adminInfoVo.setToken(StpUtil.getTokenValue())
                        //设置权限信息
                        .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(adminInfo.getAdminId()))
                        //设置角色信息
                        .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(adminInfo.getAdminId()));
                resultObject.setResult(adminInfoVo)
                        .setResponseStatus(Response.SUCCESS);

                //记录登录信息
                adminLogInLogService.newLog(adminInfo.getAdminId(),
                        IPHandler.getIP(request),
                        true);
            }else{
                resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
                //记录登录信息
                adminLogInLogService.newLog(adminInfo.getAdminId(),
                        IPHandler.getIP(request),
                        false);
            }
        }

        return resultObject.compact();
    }
}
