package com.hepsi.todoapp.dto.response;

import com.hepsi.todoapp.enums.ERole;
import com.hepsi.todoapp.model.User;
import lombok.Data;

@Data
public class RegisterResponseModel {

    private User user;
    private ERole eRole;

    public RegisterResponseModel(User user){

        this.user = user;
        this.eRole = ERole.PERSONAL_USER;
    }

}
