package com.hepsi.todoapp.todoapp.manager;

import com.hepsi.todoapp.todoapp.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenManager {

    /**
     * Saves ConfirmationToken to db
     * @param confirmationToken
     */
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    /**
     * Deletes softly ConfirmationToken by id
     * @param id
     */
    void deleteConfirmationToken(Long id);

    /**
     * Give optional of ConfirmationToken by token string
     * @param token
     * @return Optional<ConfirmationToken>
     */
    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
