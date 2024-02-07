package neko.transaction.member.feign.thirdparty;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 第三方微服务邮件服务远程调用
 */
@FeignClient(value = ServiceName.THIRD_PARTY_SERVICE, contextId = "Mail")
public interface MailFeignService {
    /**
     * 发送注册验证码
     * @param receiver 接收人邮箱
     * @param code 验证码
     * @return 响应结果
     */
    @PostMapping("mail/send_register_mail")
    ResultObject<Object> sendRegisterMail(@RequestParam String receiver, @RequestParam String code);

    /**
     * 发送密码重置验证码
     * @param receiver 接收人邮箱
     * @param code 验证码
     * @return 响应结果
     */
    @PostMapping("mail/send_password_reset_mail")
    ResultObject<Object> sendPasswordResetMail(@RequestParam String receiver, @RequestParam String code);
}
