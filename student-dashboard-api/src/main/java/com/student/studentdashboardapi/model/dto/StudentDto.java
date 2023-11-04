package com.student.studentdashboardapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.studentdashboardapi.model.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @JsonProperty("isActive")
    private boolean isActive;

    private List<Application> applications;
    @JsonIgnore
    private List<TokenDto> tokens;

}