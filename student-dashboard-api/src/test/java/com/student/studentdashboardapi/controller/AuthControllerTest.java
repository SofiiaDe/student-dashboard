package com.student.studentdashboardapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.studentdashboardapi.model.payload.request.LoginRequest;
import com.student.studentdashboardapi.model.payload.request.RegistrationRequest;
import com.student.studentdashboardapi.model.payload.response.LoginResponse;
import com.student.studentdashboardapi.model.payload.response.RegistrationResponse;
import com.student.studentdashboardapi.service.AuthService;
import com.student.studentdashboardapi.service.StudentService;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;
    @Mock
    private StudentService studentService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterTrainerWhenValidRequest() throws Exception {
        RegistrationRequest validRegistrationRequest = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .username("newStudentUsername")
                .password("newStudentPassword")
                .build();

        when(studentService.registerStudent(validRegistrationRequest)).thenReturn(registrationResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegistrationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("newStudentUsername"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("newStudentPassword"));
    }

    @Test
    void testRegisterTrainerWhenInvalidRequest() throws Exception {
        RegistrationRequest invalidRegistrationRequest = RegistrationRequest.builder()
                .firstName(null)
                .lastName(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRegistrationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Map<String, String> errors = objectMapper.readValue(response,
                            new TypeReference<>() {
                            });
                    assertEquals("First Name is required", errors.get("firstName"));
                    assertEquals("Last Name is required", errors.get("lastName"));
                });
    }

    @Test
    void testLoginShouldLoginSuccessfully() throws Exception {
        LoginRequest validLoginRequest = LoginRequest.builder()
                .username("validUsername")
                .password("validPassword")
                .build();
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("accessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void testLoginWhenNullRequestProperties() throws Exception {
        LoginRequest invalidLoginRequest = LoginRequest.builder()
                .username(null)
                .password(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Map<String, String> errors = objectMapper.readValue(response,
                            new TypeReference<>() {
                            });
                    assertEquals("Username is required", errors.get("username"));
                    assertEquals("Password is required", errors.get("password"));
                });
    }

    @Test
    void testLoginWhenEmptyRequestProperties() throws Exception {
        LoginRequest invalidLoginRequest = LoginRequest.builder()
                .username("")
                .password("")
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Map<String, String> errors = objectMapper.readValue(response,
                            new TypeReference<>() {
                            });
                    assertEquals("Username is required", errors.get("username"));
                    assertEquals("Password is required", errors.get("password"));
                });
    }


}