package com.hepsi.todoapp.todoapp.manager;

import com.hepsi.todoapp.todoapp.model.User;

import java.io.IOException;

public interface ProfileManager {

    void removeDeleteAccount(User user) throws IOException;
}
