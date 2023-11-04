package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.model.payload.request.RegistrationRequest;
import com.student.studentdashboardapi.model.payload.response.RegistrationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface StudentService {

    String generateUsername(String firstName, String lastName);

    String generatePassword();

    RegistrationResponse registerStudent(RegistrationRequest request);

    StudentDto getStudentByUsername(String username);

    Student getStudentFromHttpRequest(HttpServletRequest httpServletRequest);

}
