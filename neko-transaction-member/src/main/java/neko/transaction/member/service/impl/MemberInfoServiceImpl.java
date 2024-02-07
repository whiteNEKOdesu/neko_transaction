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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.Response;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.LoginException;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.entity.UserRoleRelation;
import neko.transaction.member.ip.IPHandler;
import neko.transaction.member.mapper.MemberInfoMapper;
import neko.transaction.member.service.MemberInfoService;
import neko.transaction.member.service.MemberLogInLogService;
import neko.transaction.member.service.UserRoleRelationService;
import neko.transaction.member.service.WeightRoleRelationService;
import neko.transaction.member.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

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
                String uid = memberInfo.getUid();
                StpUtil.login(uid);
                //获取用户详细信息
                MemberInfoVo memberInfoVo = this.baseMapper.getMemberInfoByUid(uid);

                //设置 token
                memberInfoVo.setToken(StpUtil.getTokenValue())
                        //设置权限信息
                        .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(memberInfo.getUid()))
                        //设置角色信息
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
    @Transactional(rollbackFor = Exception.class)
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
        memberInfo.setUserPassword(DigestUtils.md5DigestAsHex((userPassword + salt).getBytes()))
                .setSalt(salt);

        //添加用户
        this.baseMapper.insert(memberInfo);

        //为用户设置普通用户以及普通会员角色
        userRoleRelationService.newRelations(vo.getUid(),
                Collections.singletonList(1));
    }

    /**
     * 根据学号删除用户
     * @param uid 学号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String uid) {
        this.baseMapper.deleteById(uid);

        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleRelation::getUid, uid);
        //删除角色联系
        userRoleRelationService.remove(queryWrapper);
    }

    /**
     * 获取用户自身的详细信息
     * @return 用户自身的详细信息
     */
    @Override
    public MemberInfoVo userSelfInfo() {
        String uid = StpUtil.getLoginId().toString();

        return this.baseMapper.getMemberInfoByUid(uid)
                //设置权限信息
                .setWeightTypes(weightRoleRelationService.getWeightTypesByUid(uid))
                //设置角色信息
                .setRoleTypes(weightRoleRelationService.getRoleTypesByUid(uid));
    }

    /**
     * 根据学号获取用户公开信息
     * @param uid 学号
     * @return 用户公开信息
     */
    @Override
    public PublicMemberInfoVo publicMemberInfoByUid(String uid) {
        return this.baseMapper.getPublicMemberInfoByUid(uid);
    }

    /**
     * 添加用户余额
     * @param vo 添加用户余额vo
     */
    @Override
    public void addBalance(AddMemberBalanceVo vo) {
        this.baseMapper.addBalance(vo.getUid(), vo.getAddNumber()
                .setScale(2, BigDecimal.ROUND_DOWN));
    }

    /**
     * 用户名是否重复
     * @param userName 用户名
     * @return 用户名是否重复
     */
    @Override
    public boolean userNameIsRepeat(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<MemberInfo>().lambda()
                .eq(MemberInfo::getUserName, userName)) != null;
    }

    /**
     * 修改密码
     * @param vo 修改密码vo
     */
    @Override
    public void updateUserPassword(UpdateUserPasswordVo vo) {
        String uid = StpUtil.getLoginId().toString();
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
        if(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()).equals(memberInfo.getUserPassword())){
            String todoPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getTodoPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            MemberInfo todoMemberInfo = new MemberInfo();
            todoPassword = DigestUtils.md5DigestAsHex((todoPassword + memberInfo.getSalt()).getBytes());
            todoMemberInfo.setUid(uid)
                    .setUserPassword(todoPassword);

            this.baseMapper.updateById(todoMemberInfo);
        }else{
            throw new LoginException("密码错误");
        }
    }
}
