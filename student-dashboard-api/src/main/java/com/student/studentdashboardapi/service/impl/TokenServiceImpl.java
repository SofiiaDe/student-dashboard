package com.student.studentdashboardapi.service.impl;

import com.student.studentdashboardapi.exception.EntityNotFoundException;
import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Token;
import com.student.studentdashboardapi.model.enumeration.TokenType;
import com.student.studentdashboardapi.model.mapper.CycleAvoidingMappingContext;
import com.student.studentdashboardapi.model.mapper.StudentMapper;
import com.student.studentdashboardapi.repository.TokenRepository;
import com.student.studentdashboardapi.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_NOT_FOUND = "Cannot find the jwt token: ";

    private TokenRepository tokenRepository;
    private StudentMapper studentMapper;

    @Override
    public void saveStudentToken(StudentDto studentDto, String jwtToken) {
        Token token = Token.builder()
                .student(studentMapper.toEntity(studentDto, new CycleAvoidingMappingContext()))
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllStudentTokens(Long studentId) {
        List<Token> validStudentTokens = tokenRepository.findAllValidTokensByStudent(studentId);
        if (validStudentTokens.isEmpty()) {
            return;
        }
        validStudentTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validStudentTokens);
    }

    @Override
    public boolean updateTokenAfterLogout(String jwt) {
        var storedToken = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new EntityNotFoundException(TOKEN_NOT_FOUND + jwt));
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        return true;
    }
}
