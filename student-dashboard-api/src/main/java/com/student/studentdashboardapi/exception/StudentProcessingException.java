package com.student.studentdashboardapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StudentProcessingException extends RuntimeException {

    public StudentProcessingException(String message) {
        super(message);
    }
}
