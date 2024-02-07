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

    private static final String TEXT_PREFIX = "<!DOCTYPE html>\n" +
            "<html lang=\"\">\n" +
            "  <head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
            "    <link rel=\"icon\" href=\"favicon.ico\">\n" +
            "    <title>发生错误了!!!</title>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <noscript><strong>We're sorry but nekocloud doesn't work properly without JavaScript enabled. Please enable it to continue.</strong></noscript>\n" +
            "    <div id=\"app\"></div>\n" +
            "    <div style=\"text-align: center\">\n" +
            "      <img style=\"width:15%\" src=\"http://106.15.137.108/static/kokomi4.jpg\">\n" +
            "      <img style=\"width:15%\" src=\"http://106.15.137.108/static/kamisato1.gif\">\n" +
            "      <img style=\"width:15%\" src=\"http://106.15.137.108/static/kamisato2.gif\">\n" +
            "      <img style=\"width:15%\" src=\"http://106.15.137.108/static/kokomi6.gif\">\n" +
            "      <img style=\"width:15%\" src=\"http://106.15.137.108/static/kusakami1.gif\">\n" +
            "      <br/>\n";

    private static final String TEXT_SUFFIX = "    </div>\n" +
            "  </body>\n" +
            "</html>\n";

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
