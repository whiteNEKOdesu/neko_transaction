package neko.transaction.thirdparty.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendRegisterMail(String emailAddress, String code) throws MessagingException;

    void sendPasswordResetMail(String emailAddress, String code) throws MessagingException;
}
