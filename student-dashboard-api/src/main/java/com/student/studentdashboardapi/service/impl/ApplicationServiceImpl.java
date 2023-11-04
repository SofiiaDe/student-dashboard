package com.student.studentdashboardapi.service.impl;

import com.student.studentdashboardapi.model.dto.ApplicationDto;
import com.student.studentdashboardapi.model.entity.Application;
import com.student.studentdashboardapi.model.entity.Student;
import com.student.studentdashboardapi.model.payload.request.NewApplicationRequest;
import com.student.studentdashboardapi.model.payload.response.StudentApplicationsResponse;
import com.student.studentdashboardapi.repository.ApplicationRepository;
import com.student.studentdashboardapi.service.ApplicationService;
import com.student.studentdashboardapi.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Log4j2
public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationRepository applicationRepository;
    private StudentService studentService;

    @Override
    public void createApplication(NewApplicationRequest request, HttpServletRequest httpServletRequest) {

        Student student = studentService.getStudentFromHttpRequest(httpServletRequest);
        Application application = Application.builder()
                .student(student)
                .universityName(request.getUniversityName())
                .universityCourse(request.getUniversityCourse())
                .build();

        applicationRepository.save(application);
    }

    @Override
    public StudentApplicationsResponse getApplicationsByStudent(HttpServletRequest httpServletRequest) {
        Student student = studentService.getStudentFromHttpRequest(httpServletRequest);
        List<Application> applicationsFromDb = applicationRepository.findAllByStudent(student);

        List<ApplicationDto> applications = applicationsFromDb.stream()
                .map(a -> ApplicationDto.builder()
                        .id(a.getId())
                        .universityName(a.getUniversityName())
                        .universityCourse(a.getUniversityCourse())
                        .build()).toList();

        return StudentApplicationsResponse.builder()
                .totalNumber(applicationsFromDb.size())
                .items(applications)
                .build();
    }
}
