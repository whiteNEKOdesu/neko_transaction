package neko.transaction.member.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.Response;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.*;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.entity.UserRoleRelation;
import neko.transaction.member.feign.thirdparty.MailFeignService;
import neko.transaction.member.feign.thirdparty.OSSFeignService;
import neko.transaction.member.ip.IPHandler;
import neko.transaction.member.mapper.MemberInfoMapper;
import neko.transaction.member.service.MemberInfoService;
import neko.transaction.member.service.MemberLogInLogService;
import neko.transaction.member.service.UserRoleRelationService;
import neko.transaction.member.service.WeightRoleRelationService;
import neko.transaction.member.vo.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
    private OSSFeignService ossFeignService;

    @Resource
    private MailFeignService mailFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RSA rsa;

    /**
     * 自定义纯数字的验证码（随机4位数字，可重复）
     */
    private static final RandomGenerator RANDOM_GENERATOR = new RandomGenerator("0123456789", 6);

    /**
     * 用户登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @Override
    public ResultObject<MemberInfoVo> login(LogInVo vo, HttpServletRequest request) {
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();
        //验证图形验证码是否正确
        if(!isGraphVerifyCodeValidate(vo.getTraceId(), vo.getCode())){
            return resultObject.setResponseStatus(Response.CODE_ILLEGAL_ERROR)
                    .compact();
        }

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
     * 用户学号，密码登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @Override
    public ResultObject<MemberInfoVo> uidLogin(UidLogInVo vo, HttpServletRequest request) {
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();
        //验证图形验证码是否正确
        if(!isGraphVerifyCodeValidate(vo.getTraceId(), vo.getCode())){
            return resultObject.setResponseStatus(Response.CODE_ILLEGAL_ERROR)
                    .compact();
        }

        String uid = vo.getUid();
        //根据学号查询用户信息
        MemberInfo memberInfo = this.baseMapper.selectById(uid);

        if(memberInfo == null){
            return resultObject.setResponseStatus(Response.USER_LOG_IN_ERROR);
        }else{
            //RSA 解密获取密码
            String userPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getUserPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
            //校验 MD5 hash 后的密码
            if(DigestUtils.md5DigestAsHex((userPassword + memberInfo.getSalt()).getBytes()).equals(memberInfo.getUserPassword())){
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
     * 发送邮箱登录邮件
     * @param receiver 邮箱
     */
    @Override
    public void sendLogInCode(String receiver) {
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        MemberInfo memberInfo = this.baseMapper.selectOne(queryWrapper.lambda().eq(MemberInfo::getMail, receiver));
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String key = Constant.MEMBER_REDIS_PREFIX + "log_in_mail_code:" + receiver;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key,
                code,
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);
        ResultObject<Object> r = mailFeignService.sendLogInMail(receiver, code);
        if(r.getResponseCode() != 200){
            throw new MailSendException("邮件发送错误");
        }
    }

    /**
     * 用户邮箱登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @Override
    public ResultObject<MemberInfoVo> emailLogin(EmailLogInVo vo, HttpServletRequest request) {
        String mail = vo.getMail();
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        MemberInfo memberInfo = this.baseMapper.selectOne(queryWrapper.lambda().eq(MemberInfo::getMail, mail));
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String code = vo.getCode();
        String key = Constant.MEMBER_REDIS_PREFIX + "log_in_mail_code:" + mail;
        String todoCode = stringRedisTemplate.opsForValue().get(key);
        String uid = memberInfo.getUid();
        ResultObject<MemberInfoVo> resultObject = new ResultObject<>();

        if(code.equals(todoCode)){
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
            memberLogInLogService.newLog(uid,
                    IPHandler.getIP(request),
                    false);
        }
        //删除验证码
        stringRedisTemplate.delete(key);

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

    /**
     * 修改用户名
     * @param userName 用户名
     */
    @Override
    public void updateUserName(String userName) {
        if(!StringUtils.hasText(userName)){
            throw new IllegalArgumentException("用户名为空");
        }

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUid(StpUtil.getLoginId().toString())
                .setUserName(userName);

        this.baseMapper.updateById(memberInfo);
    }

    /**
     * 修改头像
     * @param file 图片
     * @return 修改后的头像url
     */
    @Override
    public String updateUserImagePath(MultipartFile file) {
        //step1 -> 远程调用第三方微服务，上传图片到oss
        ResultObject<String> r = ossFeignService.uploadImage(file);
        if(!r.getResponseCode().equals(200)){
            throw new ThirdPartyServiceException("thirdparty微服务远程调用异常");
        }

        String url = r.getResult();
        String uid = StpUtil.getLoginId().toString();
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        //step2 -> 远程调用第三方微服务，删除原图片
        if(memberInfo.getUserImagePath() != null){
            ResultObject<Object> deleteResult = ossFeignService.deleteFile(memberInfo.getUserImagePath());
            if(!deleteResult.getResponseCode().equals(200)){
                throw new ThirdPartyServiceException("thirdparty微服务远程调用异常");
            }
        }

        //step3 -> 修改用户信息
        MemberInfo todoUpdateMemberInfo = new MemberInfo();
        todoUpdateMemberInfo.setUid(uid)
                .setUserImagePath(url);

        this.baseMapper.updateById(todoUpdateMemberInfo);

        return url;
    }

    /**
     * 发送重置密码邮件
     * @param uid 学号
     * @return 发送的邮箱
     */
    @Override
    public String sendUserPasswordResetCode(String uid) {
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String email = memberInfo.getMail();
        String key = Constant.MEMBER_REDIS_PREFIX + "password_reset_mail_code:" + email;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key,
                code,
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);
        ResultObject<Object> r = mailFeignService.sendPasswordResetMail(email, code);

        if(r.getResponseCode() != 200){
            throw new MailSendException("邮件发送错误");
        }

        return email;
    }

    /**
     * 接收重置密码邮件后，使用验证码重置密码
     * @param vo 重置密码vo
     */
    @Override
    public void resetUserPassword(ResetUserPasswordVo vo) {
        String uid = vo.getUid();
        MemberInfo memberInfo = this.baseMapper.selectById(uid);
        if(memberInfo == null){
            throw new NoSuchResultException("无此用户");
        }

        String key = Constant.MEMBER_REDIS_PREFIX + "password_reset_mail_code:" + memberInfo.getMail();
        String todoCode = stringRedisTemplate.opsForValue().get(key);

        if(!vo.getCode().equals(todoCode)){
            throw new CodeIllegalException("验证码错误");
        }
        String todoPassword = StrUtil.str(rsa.decrypt(Base64.decode(vo.getTodoPassword()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);
        MemberInfo todoMemberInfo = new MemberInfo();
        todoPassword = DigestUtils.md5DigestAsHex((todoPassword + memberInfo.getSalt()).getBytes());
        todoMemberInfo.setUid(uid)
                .setUserPassword(todoPassword);

        this.baseMapper.updateById(todoMemberInfo);
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取登录的 Base64 图形验证码
     * @return Base64 图形验证码
     */
    @Override
    public LogInGraphVerifyCodeVo getLoginBase64GraphVerifyCode() {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        captcha.setGenerator(RANDOM_GENERATOR);

        //验证码追踪id
        String traceId = IdWorker.getTimeId();
        String key = Constant.MEMBER_REDIS_PREFIX + "login_verify_code:" + traceId;
        //向 redis 中设置验证码
        stringRedisTemplate.opsForValue().set(key,
                captcha.getCode(),
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);

        LogInGraphVerifyCodeVo vo = new LogInGraphVerifyCodeVo();
        vo.setBase64Graph(captcha.getImageBase64())
                .setTraceId(traceId);

        return vo;
    }

    /**
     * 验证图形验证码是否正确
     * @param traceId 验证码追踪id
     * @param code 验证码
     * @return 验证图形验证码是否正确
     */
    private boolean isGraphVerifyCodeValidate(String traceId, String code){
        String key = Constant.MEMBER_REDIS_PREFIX + "login_verify_code:" + traceId;
        String value = stringRedisTemplate.opsForValue().getAndDelete(key);

        return value != null && value.equals(code);
    }
}
