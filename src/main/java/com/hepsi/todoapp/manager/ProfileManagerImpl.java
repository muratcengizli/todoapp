package com.hepsi.todoapp.manager;

import com.hepsi.todoapp.model.User;
import com.hepsi.todoapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ProfileManagerImpl implements ProfileManager{

    private UserRepository userRepository;
    @Override
    public void removeDeleteAccount(User user) throws IOException {

        try{
            user.setLockedAt(null);
            user.setLocked(false);
            user.setDeletedReason(null);
            user.setReactivated(true);
            userRepository.save(user);
        }catch(Exception e){
            throw new IOException();
        }
    }
}
