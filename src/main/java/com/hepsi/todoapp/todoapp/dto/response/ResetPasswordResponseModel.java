package com.hepsi.todoapp.todoapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResetPasswordResponseModel {
    private String message;
    private HttpStatus httpStatus;
}
