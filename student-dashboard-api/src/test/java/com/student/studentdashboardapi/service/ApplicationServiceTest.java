package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.dto.ApplicationDto;
import com.student.studentdashboardapi.model.dto.StudentDto;
import com.student.studentdashboardapi.model.entity.Application;
import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.model.payload.request.NewApplicationRequest;
import com.student.studentdashboardapi.model.payload.response.StudentApplicationsResponse;
import com.student.studentdashboardapi.repository.ApplicationRepository;
import com.student.studentdashboardapi.service.impl.ApplicationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private HttpServletRequest httpServletRequest;
    private ApplicationService applicationService;

    @BeforeEach
    public void setUp() {
        applicationService = new ApplicationServiceImpl(applicationRepository, studentService);
    }

    @Test
    void testCreateApplication() {
        Student student = Student.builder()
                .username("Tom.Berry")
                .build();

        NewApplicationRequest newApplicationRequest = NewApplicationRequest.builder()
                .universityName("Cambridge University")
                .universityCourse("Java")
                .build();

        Application application = Application.builder()
                .student(student)
                .universityName("Cambridge University")
                .universityCourse("Java")
                .build();

        when(studentService.getStudentFromHttpRequest(httpServletRequest)).thenReturn(student);
        when(applicationRepository.save(any())).thenReturn(application);
        applicationService.createApplication(newApplicationRequest, httpServletRequest);
        verify(applicationRepository).save(application);
    }

    @Test
    void testGetApplicationsByStudentWhenValidStudent() {
        Student student = new Student();
        student.setUsername("john.smith");

        StudentDto studentDto = new StudentDto();
        studentDto.setUsername("john.smith");

        List<Application> applicationsFromDb = List.of(
                Application.builder()
                        .id(1L)
                        .universityName("Cambridge University")
                        .universityCourse("Java")
                        .build(),
                Application.builder()
                        .id(2L)
                        .universityName("Bonn University")
                        .universityCourse("React")
                        .build()
        );

        List<ApplicationDto> applications = List.of(
                ApplicationDto.builder()
                        .id(1L)
                        .universityName("Cambridge University")
                        .universityCourse("Java")
                        .build(),
                ApplicationDto.builder()
                        .id(2L)
                        .universityName("Bonn University")
                        .universityCourse("React")
                        .build()
        );

        StudentApplicationsResponse response = StudentApplicationsResponse.builder()
                .totalNumber(applicationsFromDb.size())
                .items(applications)
                .build();

        when(studentService.getStudentFromHttpRequest(httpServletRequest)).thenReturn(student);
        when(applicationRepository.findAllByStudent(student)).thenReturn(applicationsFromDb);

        StudentApplicationsResponse result = applicationService.getApplicationsByStudent(httpServletRequest);

        assertNotNull(result);
        assertEquals(response, result);

        verify(studentService, times(1)).getStudentFromHttpRequest(httpServletRequest);
        verify(applicationRepository, times(1)).findAllByStudent(eq(student));
    }

}
