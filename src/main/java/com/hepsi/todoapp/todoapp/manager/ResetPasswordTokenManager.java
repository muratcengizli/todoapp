package com.hepsi.todoapp.todoapp.manager;

import com.hepsi.todoapp.todoapp.dto.response.ResetPasswordResponseModel;
import com.hepsi.todoapp.todoapp.model.ResetPasswordToken;
import com.hepsi.todoapp.todoapp.model.User;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.Timestamp;

public interface ResetPasswordTokenManager {

    /**
     * Deletes the resetPasswordToken with the given id
     * @param id
     */
    void deleteResetPasswordToken(Long id);

    /**
     * Updates the given user's password to given newPassword
     * if operation is successful returns true
     * else returns false
     * @param user
     * @param newPassword
     * @return Boolean
     */
    Boolean updatePassword(User user, String newPassword) throws Exception;

    /**
     * Updates the reset password token for the user of the given emailAddress
     * @param emailAddress
     * @return ResponseEntity<ResetPasswordResponseModel>
     */
    ResponseEntity<ResetPasswordResponseModel> updateResetPasswordToken(String emailAddress) throws IOException;

    /**
     * Saves the given resetPasswordToken to db
     * @param resetPasswordToken
     */
    void saveResetPasswordToken(ResetPasswordToken resetPasswordToken);

    /**
     * Gets the ResetPasswordToken with the given reset password token
     * @param token
     * @return ResetPasswordToken
     */
    ResetPasswordToken findResetPasswordTokenByToken(String token);

    /**
     * Tells if the token is expired or not
     * if the token is expired returns true
     * else returns false
     * @param timestamp (provide the tokens createdAt timestamp)
     * @return Boolean
     */
    Boolean isTokenExpired(Timestamp timestamp);
}
