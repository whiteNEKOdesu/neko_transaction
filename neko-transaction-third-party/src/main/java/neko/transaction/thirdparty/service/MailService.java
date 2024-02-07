package neko.transaction.thirdparty.service;

import javax.mail.MessagingException;

public interface MailService {
    /**
     * 发送注册验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    void sendRegisterMail(String emailAddress, String code) throws MessagingException;

    /**
     * 发送密码重置验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    void sendPasswordResetMail(String emailAddress, String code) throws MessagingException;

    /**
     * 发送邮箱登录验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    void sendLogInMail(String emailAddress, String code) throws MessagingException;
}
