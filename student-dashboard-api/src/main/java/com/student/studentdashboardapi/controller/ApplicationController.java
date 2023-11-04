package com.student.studentdashboardapi.controller;

import com.student.studentdashboardapi.model.payload.request.NewApplicationRequest;
import com.student.studentdashboardapi.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationController extends BaseController {

    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> createApplication(@Valid @RequestBody NewApplicationRequest request,
                                               BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        applicationService.createApplication(request, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Application added successfully");
    }

    @GetMapping
    public ResponseEntity<?> getApplicationsByStudent(HttpServletRequest httpServletRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getApplicationsByStudent(httpServletRequest));
    }
}
