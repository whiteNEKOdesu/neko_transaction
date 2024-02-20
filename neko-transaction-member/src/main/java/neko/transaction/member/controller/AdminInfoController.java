package neko.transaction.member.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.service.AdminInfoService;
import neko.transaction.member.vo.AdminInfoVo;
import neko.transaction.member.vo.LogInGraphVerifyCodeVo;
import neko.transaction.member.vo.LogInVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("admin_info")
public class AdminInfoController {
    @Resource
    private AdminInfoService adminInfoService;

    /**
     * 管理员登录
     * @param vo 登录vo
     * @param request HttpServletRequest
     * @return 管理员信息vo
     */
    @PostMapping("login")
    public ResultObject<AdminInfoVo> login(@Validated @RequestBody LogInVo vo, HttpServletRequest request){
        return adminInfoService.login(vo, request);
    }

    /**
     * 获取登录的 Base64 图形验证码
     * @return Base64 图形验证码
     */
    @GetMapping("login_graph_verify_code")
    public ResultObject<LogInGraphVerifyCodeVo> logInGraphVerifyCode(){
        return ResultObject.ok(adminInfoService.getLoginBase64GraphVerifyCode());
    }
}
