package com.hepsi.todoapp.todoapp.repository;

import com.hepsi.todoapp.todoapp.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
