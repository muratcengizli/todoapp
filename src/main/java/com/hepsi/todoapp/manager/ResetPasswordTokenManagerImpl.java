package com.hepsi.todoapp.manager;

import com.hepsi.todoapp.repository.ResetPasswordTokenRepository;
import com.hepsi.todoapp.repository.UserRepository;
import com.hepsi.todoapp.dto.response.ResetPasswordResponseModel;
import com.hepsi.todoapp.model.ResetPasswordToken;
import com.hepsi.todoapp.model.User;
import com.hepsi.todoapp.util.Constants;
import com.hepsi.todoapp.util.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.sql.Timestamp;


@Service
@AllArgsConstructor
public class ResetPasswordTokenManagerImpl implements  ResetPasswordTokenManager{

    private UserRepository userRepository;
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final MailManager mailManager;

    @Override
    public void deleteResetPasswordToken(Long id) {resetPasswordTokenRepository.deleteById(id);}

    @Override
    public Boolean updatePassword(User user, String newPassword) throws Exception {
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            deleteResetPasswordToken(user.getResetPasswordToken().getId());
            userRepository.save(user);
            return true;
        }catch(Exception e) {
            throw new Exception(Constants.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public ResponseEntity<ResetPasswordResponseModel> updateResetPasswordToken(String emailAddress) throws IOException {
        try {
            User user = userRepository.findByEmailAddress(emailAddress);
            if(ObjectUtils.isEmpty(user)) {
                throw new Exception(Constants.COULD_NOT_FIND_ANY_USER_WITH_GIVEN_EMAIL);
            }
            if(!ObjectUtils.isEmpty(user.getResetPasswordToken())) {
                deleteResetPasswordToken(user.getResetPasswordToken().getId());
            }
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken(user);
            user.setResetPasswordToken(resetPasswordToken);
            saveResetPasswordToken(resetPasswordToken);
            mailManager.sendResetPasswordMail(user.getEmailAddress(), user.getResetPasswordToken().getResetPasswordToken());
        }catch (Exception e)    {
            throw new IOException(Constants.SOMETHING_WENT_WRONG);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordResponseModel("Reset password link is sent your email!", HttpStatus.OK));
    }

    @Override
    public void saveResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public ResetPasswordToken findResetPasswordTokenByToken(String token) {
        return resetPasswordTokenRepository.findResetPasswordTokenByResetPasswordToken(token);
    }

    @Override
    public Boolean isTokenExpired(Timestamp timestamp) {
        return Utility.did24HoursPassed(timestamp.getTime());
    }
}
