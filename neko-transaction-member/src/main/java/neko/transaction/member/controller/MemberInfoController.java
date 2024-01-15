package neko.transaction.member.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.service.MemberInfoService;
import neko.transaction.member.vo.LogInVo;
import neko.transaction.member.vo.MemberInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/member_info")
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
}
