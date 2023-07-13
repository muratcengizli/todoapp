package com.hepsi.todoapp.todoapp.manager;

import com.hepsi.todoapp.todoapp.dto.request.auth.EmailDTO;
import com.hepsi.todoapp.todoapp.dto.request.auth.UserRegisterDTO;
import com.hepsi.todoapp.todoapp.dto.response.RegisterResponseModel;
import com.hepsi.todoapp.todoapp.enums.ERole;
import com.hepsi.todoapp.todoapp.enums.RegisterType;
import com.hepsi.todoapp.todoapp.model.ConfirmationToken;
import com.hepsi.todoapp.todoapp.model.ResetPasswordToken;
import com.hepsi.todoapp.todoapp.model.User;
import com.hepsi.todoapp.todoapp.repository.UserRepository;
import com.hepsi.todoapp.todoapp.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthManagerImpl implements AuthManager, UserDetailsService {

    private UserRepository userRepository;
    private ConfirmationTokenManager confirmationTokenManager;
    private MailManager mailManager;
    private ResetPasswordTokenManager resetPasswordTokenManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public ResponseEntity<RegisterResponseModel> save(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setEmailAddress(userRegisterDTO.getEmailAddress());
        user.setName(userRegisterDTO.getName());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
        user.setUuid(String.valueOf(UUID.randomUUID()));
        user.setRole(ERole.PERSONAL_USER);
        user.setRegisterType(RegisterType.LOCAL);
        userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenManager.saveConfirmationToken(confirmationToken);
        mailManager.sendConfirmationMail(user.getEmailAddress(), confirmationToken.getConfirmationToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseModel(user));
    }

    @Override
    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();
        if(ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException(Constants.USER_CAN_NOT_BE_FOUND);
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenManager.deleteConfirmationToken(confirmationToken.getId());
    }

    @Override
    public Boolean resetPasswordUser(ResetPasswordToken resetPasswordToken, String newPassword) throws Exception {
        final User user = resetPasswordToken.getUser();
        if(ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException(Constants.USER_CAN_NOT_BE_FOUND);
        return resetPasswordTokenManager.updatePassword(user, newPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        final Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmailAddress(email));
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

    }

    @Override
    public Boolean existsByEmailAddress(String emailAddress) {
        return userRepository.existsByEmailAddress(emailAddress);
    }

    @Override
    public ResponseEntity<HashMap<String, String>> confirmMail(String token) throws Exception {
        HashMap<String, String> response = new HashMap<>();
        ConfirmationToken findToken = confirmationTokenManager.findConfirmationTokenByToken(token).get();

        if(ObjectUtils.isEmpty(findToken)){
            response.put("status", "notFound");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!ObjectUtils.isEmpty(findToken)) {
            confirmUser(findToken);
        } else {
            response.put("status", "notFound");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HashMap<String, String>> confirmToken(String token) throws Exception {
        HashMap<String, String> response = new HashMap<>();

        ConfirmationToken findToken = null;
        ConfirmationToken optionalConfirmationToken = confirmationTokenManager.findConfirmationTokenByToken(token).get();

        if (optionalConfirmationToken != null) {
            findToken = optionalConfirmationToken;
        }

        if(ObjectUtils.isEmpty(findToken))
            throw new Exception(Constants.CONFIRMATION_TOKEN_CAN_NOT_BE_FOUND);

        User user = findToken.getUser();

        if(ObjectUtils.isEmpty(user))
            throw new Exception(Constants.USER_CAN_NOT_BE_FOUND);

        if(!user.getConfirmationToken().getConfirmationToken().equals(token))
            throw new Exception(Constants.CONFIRMATION_TOKEN_IS_NOT_VALID);

        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> findMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HashMap<String, Boolean>> checkEmail(EmailDTO emailDTO) {
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        Boolean doesExist = userRepository.existsByEmailAddress(emailDTO.getEmailAddress());
        response.put("doesExist", doesExist);
        if(Boolean.TRUE.equals(doesExist)) return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
