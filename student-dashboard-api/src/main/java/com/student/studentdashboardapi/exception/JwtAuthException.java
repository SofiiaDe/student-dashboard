package com.student.studentdashboardapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtAuthException extends RuntimeException {

    public JwtAuthException(String errorMessage) {
        super(errorMessage);
    }
}
