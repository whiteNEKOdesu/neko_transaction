package neko.transaction.thirdparty.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.thirdparty.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@RestController
@RequestMapping("mail")
public class MailController {
    @Resource
    private MailService mailService;

    /**
     * 发送注册验证码，建议只提供给微服务远程调用
     * @param receiver 接收人邮箱
     * @param code 验证码
     * @return 响应结果
     */
    @PostMapping("send_register_mail")
    public ResultObject<Object> sendRegisterMail(@RequestParam String receiver, @RequestParam String code) throws MessagingException {
        mailService.sendRegisterMail(receiver, code);

        return ResultObject.ok();
    }

    /**
     * 发送密码重置验证码，建议只提供给微服务远程调用
     * @param receiver 接收人邮箱
     * @param code 验证码
     * @return 响应结果
     */
    @PostMapping("send_password_reset_mail")
    public ResultObject<Object> sendPasswordResetMail(@RequestParam String receiver, @RequestParam String code) throws MessagingException {
        mailService.sendPasswordResetMail(receiver, code);

        return ResultObject.ok();
    }

    /**
     * 发送邮箱登录验证码，建议只提供给微服务远程调用
     * @param receiver 接收人邮箱
     * @param code 验证码
     * @return 响应结果
     */
    @PostMapping("send_log_in_mail")
    public ResultObject<Object> sendLogInMail(@RequestParam String receiver, @RequestParam String code) throws MessagingException {
        mailService.sendLogInMail(receiver, code);

        return ResultObject.ok();
    }
}
