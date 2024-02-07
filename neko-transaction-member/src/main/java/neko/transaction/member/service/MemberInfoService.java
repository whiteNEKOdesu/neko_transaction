package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 学生信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface MemberInfoService extends IService<MemberInfo> {
    /**
     * 用户登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    ResultObject<MemberInfoVo> login(LogInVo vo, HttpServletRequest request);

    /**
     * 用户学号，密码登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    ResultObject<MemberInfoVo> uidLogin(UidLogInVo vo, HttpServletRequest request);

    /**
     * 发送邮箱登录邮件
     * @param receiver 邮箱
     */
    void sendLogInCode(String receiver);

    /**
     * 分页查询学生及所属二级学院，专业，班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<MemberWithSchoolInfoVo> memberWithSchoolInfoPageQuery(QueryVo vo);

    /**
     * 添加用户
     * @param vo 添加用户vo
     */
    void newMemberInfo(NewMemberInfoVo vo);

    /**
     * 根据学号删除用户
     * @param uid 学号
     */
    void deleteById(String uid);

    /**
     * 获取用户自身的详细信息
     * @return 用户自身的详细信息
     */
    MemberInfoVo userSelfInfo();

    /**
     * 根据学号获取用户公开信息
     * @param uid 学号
     * @return 用户公开信息
     */
    PublicMemberInfoVo publicMemberInfoByUid(String uid);

    /**
     * 添加用户余额
     * @param vo 添加用户余额vo
     */
    void addBalance(AddMemberBalanceVo vo);

    /**
     * 用户名是否重复
     * @param userName 用户名
     * @return 用户名是否重复
     */
    boolean userNameIsRepeat(String userName);

    /**
     * 修改密码
     * @param vo 修改密码vo
     */
    void updateUserPassword(UpdateUserPasswordVo vo);

    /**
     * 修改用户名
     * @param userName 用户名
     */
    void updateUserName(String userName);

    /**
     * 修改头像
     * @param file 图片
     * @return 修改后的头像url
     */
    String updateUserImagePath(MultipartFile file);

    /**
     * 发送重置密码邮件
     * @param uid 学号
     * @return 发送的邮箱
     */
    String sendUserPasswordResetCode(String uid);

    /**
     * 接收重置密码邮件后，使用验证码重置密码
     * @param vo 重置密码vo
     */
    void resetUserPassword(ResetUserPasswordVo vo);
}
