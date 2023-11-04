package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.dto.StudentDto;

public interface TokenService {

    void saveStudentToken(StudentDto studentDto, String jwtToken);

    void revokeAllStudentTokens(Long studentId);

    boolean updateTokenAfterLogout(String jwt);

}
