package com.hepsi.todoapp.todoapp.manager;

import com.hepsi.todoapp.todoapp.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@AllArgsConstructor
@Service
public class MailManagerImpl implements MailManager {

    private Environment environment;
    private JavaMailSender mailSender;
    @Override
    public void sendConfirmationMail(String userMail, String token) throws MailParseException {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            String validationLink = environment.getProperty("BASE_FRONTEND_URL")
                    + Constants.CONFIRM_CREATION_URL + "?token="
                    + token;

            String msg = Constants.TY_FOR_REGISTERING + "<br>"
                    + "<a href='"+ validationLink +"'>" + validationLink + "</a>";

            message.setSubject(Constants.MAIL_CONFIRMATION_LINK);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(environment.getProperty("spring.mail.username"));
            helper.setTo(userMail);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(MailManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendResetPasswordMail(String userMail, String token) throws MailParseException {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            String validationLink = environment.getProperty("BASE_FRONTEND_URL")
                    + Constants.RESET_PASSWORD_URL + "?token="
                    + token;

            String msg = Constants.RESET_PASSWORD_SUBJECT + "<br>"
                    + "<a href='"+ validationLink +"'>" + validationLink + "</a>";

            message.setSubject(Constants.MAIL_RESET_PASSWORD_LINK);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(environment.getProperty("spring.mail.username"));
            helper.setTo(userMail);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(MailManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
