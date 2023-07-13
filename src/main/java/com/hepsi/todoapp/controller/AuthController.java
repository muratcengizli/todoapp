package com.hepsi.todoapp.controller;

import com.hepsi.todoapp.dto.request.auth.EmailDTO;
import com.hepsi.todoapp.dto.request.auth.LoginDTO;
import com.hepsi.todoapp.dto.request.auth.ResetPasswordDTO;
import com.hepsi.todoapp.dto.response.ResetPasswordResponseModel;
import com.hepsi.todoapp.manager.AuthManager;
import com.hepsi.todoapp.manager.ResetPasswordTokenManager;
import com.hepsi.todoapp.model.ResetPasswordToken;
import com.hepsi.todoapp.manager.ProfileManager;
import com.hepsi.todoapp.model.User;
import com.hepsi.todoapp.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthManager authManager;
    private final ResetPasswordTokenManager resetPasswordTokenManager;
    private final ProfileManager profileManager;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthManager authManager, ResetPasswordTokenManager resetPasswordTokenManager, ProfileManager profileManager, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.resetPasswordTokenManager = resetPasswordTokenManager;
        this.profileManager = profileManager;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/confirm-creation")
    public ResponseEntity<HashMap<String, String>> confirmMail(@RequestParam("token") String token) throws Exception {
        return authManager.confirmMail(token);
    }

    @GetMapping("/confirm-sub-account-creation")
    public ResponseEntity<HashMap<String, String>> confirmSubAccount(@RequestParam("token") String token) throws Exception {
        return authManager.confirmToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<List<Object>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmailAddress(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();
        if(user.getReactivated().equals(Boolean.TRUE))  user.setReactivated(Boolean.FALSE);
        if(user.getLocked().equals(Boolean.TRUE)) profileManager.removeDeleteAccount(user);

        List<Object> res = new ArrayList<>();
        res.add(user);

        Map listJwt = new HashMap<String, String>();
        listJwt.put("accessToken", jwt);
        listJwt.put("tokenType", "Bearer");
        res.add(listJwt);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PERSONAL_USER')")
    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        return authManager.findMe();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResetPasswordResponseModel> forgotPasswordProcess(@RequestBody EmailDTO emailDTO) throws IOException {
        return resetPasswordTokenManager.updateResetPasswordToken(emailDTO.getEmailAddress());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponseModel> resetPasswordProcess(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {

        ResetPasswordToken resetPasswordToken = resetPasswordTokenManager.findResetPasswordTokenByToken(resetPasswordDTO.getToken());

        if(Objects.equals(Boolean.TRUE, resetPasswordTokenManager.isTokenExpired(resetPasswordToken.getCreatedAt()))) {
            throw new Exception("Your token is expired");
        }

        if(Objects.equals(Boolean.TRUE, authManager.resetPasswordUser(resetPasswordToken, resetPasswordDTO.getPassword())))   {
            return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordResponseModel("Password updated!", HttpStatus.OK));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResetPasswordResponseModel("", HttpStatus.BAD_REQUEST));
    }
}
