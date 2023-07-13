package com.hepsi.todoapp.manager;

import com.hepsi.todoapp.dto.request.auth.EmailDTO;
import com.hepsi.todoapp.dto.request.auth.UserRegisterDTO;
import com.hepsi.todoapp.dto.response.RegisterResponseModel;
import com.hepsi.todoapp.model.ConfirmationToken;
import com.hepsi.todoapp.model.ResetPasswordToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

public interface AuthManager {

    /**
     * Converts PersonalUserRegisterDTO to PersonalUser and save to db as a user with LOCAL register type
     * @param userRegisterDTO - userRegisterDTO
     * @return RegisterResponseModel
     */
    ResponseEntity<RegisterResponseModel> save(UserRegisterDTO userRegisterDTO);

    /**
     * Sets the enabled status of the user of the confirmationToken to true in db
     * @param confirmationToken - confirmationToken
     */
    void confirmUser(ConfirmationToken confirmationToken);

    /**
     * Sets the given newPassword to the user with the given resetPasswordToken
     * if the operation is successful returns true
     * else returns false
     * @param resetPasswordToken - resetPasswordToken
     * @param newPassword - newPassword
     * @return Boolean
     */
    Boolean resetPasswordUser(ResetPasswordToken resetPasswordToken, String newPassword) throws Exception;

    /**
     * If the given email is present in db, gives that user's UserDetails
     * else returns new UsernameNotFoundException
     * @param email - email
     * @return UserDetails
     */
    UserDetails loadUserByUsername(String email);

    /**
     * if the given emailAddress is present in db returns true
     * else returns false
     * @param emailAddress - emailAddress
     * @return Boolean
     */
    Boolean existsByEmailAddress(String emailAddress);

    /**
     * confirms mail
     * @param token
     * @return ResponseEntity<HashMap<String, String>>
     */
    ResponseEntity<HashMap<String, String>> confirmMail(String token) throws Exception;

    /**
     * tells if the token is valid or not
     * @param token
     * @return ResponseEntity<HashMap<String, String>>
     */
    ResponseEntity<HashMap<String, String>> confirmToken(String token) throws Exception;

    /**
     * If the given user is present in db, gives the user info as ResponseEntity
     * else throws new UsernameNotFoundException
     * @return ResponseEntity.status(HttpStatus.OK)(ResponseEntity)
     */
    ResponseEntity<Object> findMe();

    /**
     * If the given email is present in db, gives "doesExist": true as a HashMap<String, Boolean>
     * else gives "doesExist": false as a HashMap<String, Boolean>
     * @param emailDTO
     * @return ResponseEntity.status(HttpStatus.OK)(ResponseEntity)
     */
    ResponseEntity<HashMap<String, Boolean>> checkEmail(EmailDTO emailDTO);
}
