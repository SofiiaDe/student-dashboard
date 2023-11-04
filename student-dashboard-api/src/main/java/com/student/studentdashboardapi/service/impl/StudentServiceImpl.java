package com.student.studentdashboardapi.service.impl;

import com.student.studentdashboardapi.exception.EntityNotFoundException;
import com.student.studentdashboardapi.exception.JwtAuthException;
import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.model.mapper.CycleAvoidingMappingContext;
import com.student.studentdashboardapi.model.mapper.StudentMapper;
import com.student.studentdashboardapi.model.payload.request.RegistrationRequest;
import com.student.studentdashboardapi.model.payload.response.RegistrationResponse;
import com.student.studentdashboardapi.repository.StudentRepository;
import com.student.studentdashboardapi.service.JwtService;
import com.student.studentdashboardapi.service.StudentService;
import com.student.studentdashboardapi.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.student.studentdashboardapi.utils.Constants.STUDENT_NOT_FOUND_BY_USERNAME;

@Service
@AllArgsConstructor
@Log4j2
public class StudentServiceImpl implements StudentService {

    private static final String AUTH_HEADER_NOT_FOUND = "HTTP request does not contain 'Authorization' header with Bearer token";
    private StudentRepository studentRepository;
    private StudentMapper studentMapper;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private Random random;

    @Override
    public String generateUsername(String firstName, String lastName) {

        String baseUsername =
                StringUtils.capitalize(firstName) + "." + StringUtils.capitalize(lastName);
        String username = baseUsername;
        int suffix = 1;

        while (studentRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        log.info("Generated username: {}", username);
        return username;
    }

    @Override
    public String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public RegistrationResponse registerStudent(RegistrationRequest request) {

        String username = generateUsername(request.getFirstName(),
                request.getLastName());
        String password = generatePassword();

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .password(passwordEncoder.encode(password))
                .isActive(true)
                .build();

        studentRepository.save(student);

        return RegistrationResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public StudentDto getStudentByUsername(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_BY_USERNAME + username));
        return studentMapper.toDto(student, new CycleAvoidingMappingContext());
    }

    @Override
    public Student getStudentFromHttpRequest(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(Constants.BEARER)) {
            throw new JwtAuthException(AUTH_HEADER_NOT_FOUND);
        }
        String accessToken = authHeader.substring(7);
        String username = jwtService.extractUsername(accessToken);

        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_BY_USERNAME + username));
    }

}
