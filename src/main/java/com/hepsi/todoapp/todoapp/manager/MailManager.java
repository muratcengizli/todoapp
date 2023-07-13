package com.hepsi.todoapp.todoapp.manager;

import org.springframework.mail.MailParseException;
public interface MailManager {

    /**
     * Sends a confirmation mail to the user with given userMail
     * The confirmation link to be clicked will have the given token
     * @param userMail
     * @param token
     */
    void sendConfirmationMail(String userMail, String token) throws MailParseException;

    /**
     * Sends a reset password mail to the user with given userMail
     * The reset password link to be clicked will have the given token
     * @param userMail
     * @param token
     */
    void sendResetPasswordMail(String userMail, String token) throws MailParseException;
}
