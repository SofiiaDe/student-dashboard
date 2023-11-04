package com.student.studentdashboardapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Long id;

    @NotBlank(message = "University Name is required")
    private String universityName;

    @NotBlank(message = "University Course is required")
    private String universityCourse;

}
