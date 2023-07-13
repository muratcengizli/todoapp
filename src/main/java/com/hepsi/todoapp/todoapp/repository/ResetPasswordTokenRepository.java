package com.hepsi.todoapp.todoapp.repository;

import com.hepsi.todoapp.todoapp.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    ResetPasswordToken findResetPasswordTokenByResetPasswordToken(String token);
}
