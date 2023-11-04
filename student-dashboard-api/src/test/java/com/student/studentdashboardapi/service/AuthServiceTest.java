package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.exception.EntityNotFoundException;
import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.payload.request.LoginRequest;
import com.student.studentdashboardapi.model.payload.response.LoginResponse;
import com.student.studentdashboardapi.service.impl.AuthServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private StudentService studentService;
    @Mock
    private TokenService tokenService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl(studentService, tokenService, jwtService,
                authenticationManager);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testLoginWhenValidRequest() {
        String username = "testUser";
        String password = "testPassword";

        StudentDto studentDto = StudentDto.builder()
                .username(username)
                .password(password)
                .build();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        when(jwtService.generateToken(studentDto)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(studentDto)).thenReturn(refreshToken);
        when(studentService.getStudentByUsername(username)).thenReturn(studentDto);

        LoginResponse result = authService.login(loginRequest);
        assertNotNull(result.getAccessToken());
        assertNotNull(result.getRefreshToken());
        assertEquals(result.getAccessToken(), jwtToken);
        assertEquals(result.getRefreshToken(), refreshToken);
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void testLoginWhenUserNotFound() {
        String username = "nonExistentUser";
        String password = "testPassword";

        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                ));

        doThrow(EntityNotFoundException.class).when(
                studentService).getStudentByUsername(any(String.class));

        assertThrows(EntityNotFoundException.class,
                () -> authService.login(loginRequest));

    }

    @Test
    void testLoginWhenWrongPassword() {
        String username = "testUser";
        String wrongPassword = "wrongPassword";

        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(wrongPassword)
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testLoginWhenNullUsername() {
        String password = "testPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(password);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void testLoginWhenNullPassword() {
        String username = "testUser";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }


}
