package com.hepsi.todoapp.repository;

import com.hepsi.todoapp.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    ResetPasswordToken findResetPasswordTokenByResetPasswordToken(String token);
}
