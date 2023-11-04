package com.student.studentdashboardapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.studentdashboardapi.model.dto.ApplicationDto;
import com.student.studentdashboardapi.model.payload.request.NewApplicationRequest;
import com.student.studentdashboardapi.model.payload.response.StudentApplicationsResponse;
import com.student.studentdashboardapi.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateApplicationWhenValidRequest() throws Exception {
        NewApplicationRequest request = NewApplicationRequest.builder()
                .universityName("Cambridge University")
                .universityCourse("Java")
                .build();

        doNothing().when(applicationService).createApplication(eq(request), any());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Application added successfully"));
    }

    @Test
    void testAddTrainingWhenInvalidRequest() throws Exception {
        ApplicationDto invalidApplicationDtoRequest = ApplicationDto.builder()
                .universityName(null)
                .universityCourse(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidApplicationDtoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(applicationService, never()).createApplication(any(NewApplicationRequest.class), any(HttpServletRequest.class));
    }

    @Test
    void testGetApplicationsByStudentWhenValidRequest() throws Exception {

        List<ApplicationDto> applications = List.of(
                ApplicationDto.builder()
                        .universityName("Cambridge University")
                        .universityCourse("Java")
                        .build(),
                ApplicationDto.builder()
                        .universityName("Bonn University")
                        .universityCourse("React")
                        .build());

        StudentApplicationsResponse response = StudentApplicationsResponse.builder()
                .totalNumber(applications.size())
                .items(applications)
                .build();

        when(applicationService.getApplicationsByStudent(any())).thenReturn(response);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/applications")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].universityName").value("Cambridge University"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].universityCourse").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].universityName").value("Bonn University"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].universityCourse").value("React"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNumber").value(2));

        verify(applicationService).getApplicationsByStudent(any());
    }

}
