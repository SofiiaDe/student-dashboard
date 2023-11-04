package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.dto.StudentDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(StudentDto studentDto);

    String generateRefreshToken(StudentDto studentDto);

}
