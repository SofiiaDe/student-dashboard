package com.student.studentdashboardapi.service.impl;

import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.payload.request.LoginRequest;
import com.student.studentdashboardapi.model.payload.response.LoginResponse;
import com.student.studentdashboardapi.service.AuthService;
import com.student.studentdashboardapi.service.JwtService;
import com.student.studentdashboardapi.service.StudentService;
import com.student.studentdashboardapi.service.TokenService;
import com.student.studentdashboardapi.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StudentService studentService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );

        StudentDto studentDto = studentService.getStudentByUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(studentDto);
        String refreshToken = jwtService.generateRefreshToken(studentDto);
        tokenService.revokeAllStudentTokens(studentDto.getId());
        tokenService.saveStudentToken(studentDto, jwtToken);

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(Constants.AUTHORIZATION);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith(Constants.BEARER)) {
            return;
        }
        jwt = authHeader.substring(7);
        if (tokenService.updateTokenAfterLogout(jwt)) {
            SecurityContextHolder.clearContext();
        }
    }

}
