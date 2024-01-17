package neko.transaction.member.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.Response;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.ip.IPHandler;
import neko.transaction.member.mapper.MemberInfoMapper;
import neko.transaction.member.service.MemberInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.service.MemberLogInLogService;
import neko.transaction.member.service.UserRoleRelationService;
import neko.transaction.member.service.WeightRoleRelationService;
import neko.transaction.member.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo> implements MemberInfoService {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private MemberLogInLogService memberLogInLogService;

    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Resource
    private RSA rsa;

    /**
     * 用户登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @Override
    public ResultObject<MemberInfoVo> login(LogInVo vo, HttpServletRequest request) {
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();
        //根据用户名查询用户信息
        MemberInfo memberInfo = this.baseMapper.selectOne(new QueryWrapper<MemberInfo>().lambda()
                .eq(MemberInfo::getUserName, vo.getUserName()));

        if(memberInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            //RSA 解密获取密码
            String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            //校验 MD5 hash 后的密码
            if(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()).equals(memberInfo.getUserPassword())){
                StpUtil.login(memberInfo.getUid());
                MemberInfoVo memberInfoVo = new MemberInfoVo();
                BeanUtil.copyProperties(memberInfo, memberInfoVo);

                memberInfoVo.setToken(StpUtil.getTokenValue())
                        .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(memberInfo.getUid()))
                        .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(memberInfo.getUid()));
                resultObject.setResult(memberInfoVo)
                        .setResponseStatus(Response.SUCCESS);

                //记录登录信息
                memberLogInLogService.newLog(memberInfo.getUid(),
                        IPHandler.getIP(request),
                        true);
            }else{
                resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
                //记录登录信息
                memberLogInLogService.newLog(memberInfo.getUid(),
                        IPHandler.getIP(request),
                        false);
            }
        }

        return resultObject.compact();
    }

    /**
     * 分页查询学生及所属二级学院，专业，班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<MemberWithSchoolInfoVo> memberWithSchoolInfoPageQuery(QueryVo vo) {
        Page<MemberWithSchoolInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String classId = (String) vo.getObjectId();
        //设置分页查询结果
        page.setRecords(this.baseMapper.memberWithSchoolInfoPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                classId));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.memberWithSchoolInfoPageQueryNumber(vo.getQueryWords(), classId));

        return page;
    }

    /**
     * 添加用户
     * @param vo 添加用户vo
     */
    @Override
    public void newMemberInfo(NewMemberInfoVo vo) {
        MemberInfo memberInfo = new MemberInfo();
        BeanUtil.copyProperties(vo, memberInfo);

        String idCardNumber = vo.getIdCardNumber();
        int length = idCardNumber.length();
        //默认密码为 学号 + 身份证后 4 位
        String userPassword = vo.getUid() + idCardNumber.substring(length - 4, length);
        //生成盐
        String salt = Arrays.toString(RandomUtil.randomBytes(10));
        //设置 MD5 hash 后的密码
        memberInfo.setUserPassword(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()))
                .setSalt(salt);

        //添加用户
        this.baseMapper.insert(memberInfo);
    }
}
