package com.student.studentdashboardapi.service.impl;

import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String STUDENT_NOT_FOUND_BY_USERNAME = "Cannot find the student with username = ";

    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_BY_USERNAME + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(student.getUsername())
                .password(student.getPassword())
                .roles()
                .build();
    }

}