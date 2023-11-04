package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.payload.request.LoginRequest;
import com.student.studentdashboardapi.model.payload.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
