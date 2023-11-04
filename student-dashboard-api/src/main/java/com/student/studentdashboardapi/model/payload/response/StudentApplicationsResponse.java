package com.student.studentdashboardapi.model.payload.response;

import com.student.studentdashboardapi.model.dto.ApplicationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentApplicationsResponse {

    private Integer totalNumber;
    private List<ApplicationDto> items;
}
