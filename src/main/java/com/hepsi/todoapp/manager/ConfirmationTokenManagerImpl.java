package com.hepsi.todoapp.manager;

import com.hepsi.todoapp.repository.ConfirmationTokenRepository;
import com.hepsi.todoapp.model.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;


@AllArgsConstructor
@Service
public class ConfirmationTokenManagerImpl implements ConfirmationTokenManager{

    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public void deleteConfirmationToken(Long id) {
        confirmationTokenRepository.deleteById(id);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }
}
