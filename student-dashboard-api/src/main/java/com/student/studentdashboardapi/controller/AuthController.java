package com.student.studentdashboardapi.controller;

import com.student.studentdashboardapi.model.payload.request.LoginRequest;
import com.student.studentdashboardapi.model.payload.request.RegistrationRequest;
import com.student.studentdashboardapi.service.AuthService;
import com.student.studentdashboardapi.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController extends BaseController {

    private AuthService authService;
    private StudentService studentService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegistrationRequest request,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.registerStudent(request));
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authService.logout(request, response, authentication);
    }

    @GetMapping("/info")
    public ResponseEntity<String> getAppInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("student-dashboard API is healthy");
    }

}