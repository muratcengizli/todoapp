package com.hepsi.todoapp.todoapp.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailDTO {

    @NotNull(message = "email address is mandatory!")
    @Email(
            message = "Email is not valid",
            regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String emailAddress;
}
