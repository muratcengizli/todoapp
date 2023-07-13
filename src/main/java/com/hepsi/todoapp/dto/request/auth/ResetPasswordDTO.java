package com.hepsi.todoapp.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordDTO {

    @NotNull(message = "token is mandatory!")
    private String token;

    @NotNull(message = "password is mandatory!")
    @Size(min = 8, max = 50, message = "password length must be between 8 and 50 characters long!")
    private String password;
}

