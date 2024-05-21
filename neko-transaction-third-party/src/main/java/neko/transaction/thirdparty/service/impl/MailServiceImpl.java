package neko.transaction.thirdparty.service.impl;

import neko.transaction.thirdparty.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static final String TEXT_PREFIX = """
            <!DOCTYPE html>
            <html lang="">
              <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width,initial-scale=1">
                <link rel="icon" href="favicon.ico">
                <title>发生错误了!!!</title>
              </head>
              <body>
                <noscript><strong>We're sorry but nekocloud doesn't work properly without JavaScript enabled. Please enable it to continue.</strong></noscript>
                <div id="app"></div>
                <div style="text-align: center">
            """;

    private static final String TEXT_SUFFIX = """
                </div>
              </body>
            </html>
            """;

    /**
     * 发送注册验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    @Override
    public void sendRegisterMail(String emailAddress, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        //发送者
        mimeMessageHelper.setFrom(from);
        //发送给哪个地址
        mimeMessageHelper.setTo(emailAddress);
        //标题
        mimeMessageHelper.setSubject("neko_transaction注册邮件");
        //正文
        mimeMessageHelper.setText(TEXT_PREFIX +
                "      neko_transaction注册验证码为 <b>" + code + "</b>，有效时间5分钟，如果非本人操作，请不要处理此验证码" +
                TEXT_SUFFIX, true);

        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送密码重置验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    @Override
    public void sendPasswordResetMail(String emailAddress, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        //发送者
        mimeMessageHelper.setFrom(from);
        //发送给哪个地址
        mimeMessageHelper.setTo(emailAddress);
        //标题
        mimeMessageHelper.setSubject("neko_transaction密码重置邮件");
        //正文
        mimeMessageHelper.setText(TEXT_PREFIX +
                "      neko_transaction密码重置验证码为 <b>" + code + "</b>，有效时间5分钟，如果非本人操作，请不要处理此验证码" +
                TEXT_SUFFIX, true);

        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送邮箱登录验证码
     * @param emailAddress 接收人邮箱
     * @param code 验证码
     */
    @Override
    public void sendLogInMail(String emailAddress, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        //发送者
        mimeMessageHelper.setFrom(from);
        //发送给哪个地址
        mimeMessageHelper.setTo(emailAddress);
        //标题
        mimeMessageHelper.setSubject("neko_transaction登录邮件");
        //正文
        mimeMessageHelper.setText(TEXT_PREFIX +
                "      neko_transaction登录验证码为 <b>" + code + "</b>，有效时间5分钟，如果非本人操作，请不要处理此验证码" +
                TEXT_SUFFIX, true);

        javaMailSender.send(mimeMessage);
    }
}
