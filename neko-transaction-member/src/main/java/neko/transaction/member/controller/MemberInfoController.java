package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.service.MemberInfoService;
import neko.transaction.member.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("member_info")
public class MemberInfoController {
    @Resource
    private MemberInfoService memberInfoService;

    /**
     * 用户用户名登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @PostMapping("login")
    public ResultObject<MemberInfoVo> logIn(@Validated @RequestBody LogInVo vo, HttpServletRequest request){
        return memberInfoService.login(vo, request);
    }

    /**
     * 用户学号，密码登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 用户信息vo
     */
    @PostMapping("uid_login")
    public ResultObject<MemberInfoVo> uidLogIn(@Validated @RequestBody UidLogInVo vo, HttpServletRequest request){
        return memberInfoService.uidLogin(vo, request);
    }

    /**
     * 分页查询学生及所属二级学院，专业，班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @PostMapping("member_with_school_info_page_query")
    public ResultObject<Page<MemberWithSchoolInfoVo>> memberWithSchoolInfoPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(memberInfoService.memberWithSchoolInfoPageQuery(vo));
    }

    /**
     * 添加用户
     * @param vo 添加用户vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PutMapping("new_member_info")
    public ResultObject<Object> newMemberInfo(@Validated @RequestBody NewMemberInfoVo vo){
        memberInfoService.newMemberInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 管理员根据学号删除用户
     * @param uid 学号
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @DeleteMapping("delete_by_id")
    public ResultObject<Object> deleteById(@RequestParam String uid){
        memberInfoService.deleteById(uid);

        return ResultObject.ok();
    }

    /**
     * 获取用户自身的详细信息
     * @return 用户自身的详细信息
     */
    @SaCheckLogin
    @GetMapping("user_self_info")
    public ResultObject<MemberInfoVo> userSelfInfo(){
        return ResultObject.ok(memberInfoService.userSelfInfo());
    }

    /**
     * 根据学号获取用户公开信息
     * @param uid 学号
     * @return 用户公开信息
     */
    @GetMapping("public_member_info")
    public ResultObject<PublicMemberInfoVo> publicMemberInfo(@RequestParam String uid){
        return ResultObject.ok(memberInfoService.publicMemberInfoByUid(uid));
    }

    /**
     * 添加用户余额，建议只提供给微服务远程调用
     * @param vo 添加用户余额vo
     * @return 响应结果
     */
    @PostMapping("add_balance")
    public ResultObject<Object> addBalance(@Validated @RequestBody AddMemberBalanceVo vo){
        memberInfoService.addBalance(vo);

        return ResultObject.ok();
    }

    /**
     * 用户名是否重复
     * @param userName 用户名
     * @return 用户名是否重复
     */
    @GetMapping("user_name_is_repeat")
    public ResultObject<Boolean> userNameIsRepeat(@RequestParam String userName){
        return ResultObject.ok(memberInfoService.userNameIsRepeat(userName));
    }

    /**
     * 修改密码
     * @param vo 修改密码vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("update_user_password")
    public ResultObject<Object> updateUserPassword(@Validated @RequestBody UpdateUserPasswordVo vo){
        memberInfoService.updateUserPassword(vo);

        return ResultObject.ok();
    }

    /**
     * 修改用户名
     * @param userName 用户名
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("update_user_name")
    public ResultObject<Object> updateUserName(@RequestParam String userName){
        memberInfoService.updateUserName(userName);

        return ResultObject.ok();
    }

    /**
     * 修改头像
     * @param file 图片
     * @return 修改后的头像url
     */
    @SaCheckLogin
    @PostMapping("update_user_image_path")
    public ResultObject<String> updateUserImagePath(@RequestPart MultipartFile file){
        return ResultObject.ok(memberInfoService.updateUserImagePath(file));
    }

    /**
     * 发送密码重置邮件
     * @param uid 学号
     * @return 发送的邮箱
     */
    @PostMapping("send_user_password_reset_code")
    public ResultObject<String> sendPasswordResetCode(@RequestParam String uid){
        return ResultObject.ok(memberInfoService.sendUserPasswordResetCode(uid));
    }

    /**
     * 重置密码
     * @param vo 重置密码vo
     * @return 响应结果
     */
    @PostMapping("reset_user_password")
    public ResultObject<Object> resetUserPassword(@Validated @RequestBody ResetUserPasswordVo vo){
        memberInfoService.resetUserPassword(vo);

        return ResultObject.ok();
    }

    /**
     * 发送邮箱登录邮件
     * @param receiver 邮箱
     * @return 响应结果
     */
    @PostMapping("send_log_in_code")
    public ResultObject<Object> sendLogInCode(@RequestParam String receiver){
        memberInfoService.sendLogInCode(receiver);

        return ResultObject.ok();
    }
}
