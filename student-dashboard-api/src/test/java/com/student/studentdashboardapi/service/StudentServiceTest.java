package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.exception.EntityNotFoundException;
import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.model.mapper.CycleAvoidingMappingContext;
import com.student.studentdashboardapi.model.mapper.StudentMapper;
import com.student.studentdashboardapi.model.payload.request.RegistrationRequest;
import com.student.studentdashboardapi.model.payload.response.RegistrationResponse;
import com.student.studentdashboardapi.repository.StudentRepository;
import com.student.studentdashboardapi.service.impl.StudentServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;
    private Validator validator;


    @BeforeEach
    public void setUp() {
        Random random = new Random();
        studentService = new StudentServiceImpl(studentRepository, studentMapper, jwtService, passwordEncoder, random);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGenerateUsernameWhenValidRequest() {
        String firstName = "John";
        String lastName = "Doe";

        when(studentRepository.existsByUsername(anyString())).thenReturn(false);

        String generatedUsername = studentService.generateUsername(firstName, lastName);

        verify(studentRepository, times(1)).existsByUsername(anyString());
        assertNotNull(generatedUsername);
        assertTrue(generatedUsername.contains(firstName));
        assertTrue(generatedUsername.contains(lastName));
        assertEquals("John.Doe", generatedUsername);
    }

    @Test
    void testGeneratePassword() {
        String generatedPassword = studentService.generatePassword();
        assertNotNull(generatedPassword);
        assertEquals(10, generatedPassword.length());
    }

    @Test
    void testRegisterUserWhenValidRequest() {
        RegistrationRequest validRegistrationRequest = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        RegistrationResponse createdStudent = studentService.registerStudent(validRegistrationRequest);

        verify(studentRepository, times(1)).save(any(Student.class));
        assertNotNull(createdStudent);
        assertEquals("John.Doe", createdStudent.getUsername());
    }

    @Test
    void testRegisterStudentWhenNullRequest() {
        assertThrows(NullPointerException.class, () -> studentService.registerStudent(null));
    }

    @Test
    void testRegisterStudentWhenMissingFirstName() {
        RegistrationRequest request = RegistrationRequest.builder()
                .lastName("Doe")
                .build();

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("First Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testCreateTrainerWhenMissingLastName() {
        RegistrationRequest request = RegistrationRequest
                .builder()
                .firstName("John")
                .build();

        Set<ConstraintViolation<RegistrationRequest
                >> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("Last Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testGetUserByUsernameWhenValidUsername() {
        Student student = new Student();
        student.setUsername("john.smith");

        StudentDto studentDto = new StudentDto();
        studentDto.setUsername("john.smith");

        when(studentRepository.findByUsername("john.smith")).thenReturn(Optional.of(student));
        when(studentMapper.toDto(any(Student.class), any(CycleAvoidingMappingContext.class))).thenReturn(studentDto);

        StudentDto result = studentService.getStudentByUsername("john.smith");

        assertNotNull(result);
        assertEquals(studentDto.getUsername(), result.getUsername());

        verify(studentRepository, times(1)).findByUsername("john.smith");
        verify(studentMapper, times(1)).toDto(eq(student), any(CycleAvoidingMappingContext.class));
    }

    @Test
    void testGetUserByUsernameWhenInvalidUsername() {
        when(studentRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> studentService.getStudentByUsername("nonexistentuser"));

        verify(studentRepository, times(1)).findByUsername("nonexistentuser");
        verify(studentMapper, never()).toDto(any(Student.class), any(CycleAvoidingMappingContext.class));
    }

}