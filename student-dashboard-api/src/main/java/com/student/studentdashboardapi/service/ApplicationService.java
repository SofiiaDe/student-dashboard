package com.student.studentdashboardapi.service;

import com.student.studentdashboardapi.model.payload.request.NewApplicationRequest;
import com.student.studentdashboardapi.model.payload.response.StudentApplicationsResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface ApplicationService {

    void createApplication(NewApplicationRequest request, HttpServletRequest httpServletRequest);

    StudentApplicationsResponse getApplicationsByStudent(HttpServletRequest httpServletRequest);
}
