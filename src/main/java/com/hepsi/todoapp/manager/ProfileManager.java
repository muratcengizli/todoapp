package com.hepsi.todoapp.manager;

import com.hepsi.todoapp.model.User;

import java.io.IOException;

public interface ProfileManager {

    void removeDeleteAccount(User user) throws IOException;
}
