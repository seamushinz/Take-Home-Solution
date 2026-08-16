package com.challenge.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeApiSecurityTests {

    private static final String EMPLOYEE_API_PATH = "/api/v1/employee";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllEmployeesWithoutCredentialsReturnsUnauthorized() throws Exception {
        mockMvc.perform(get(EMPLOYEE_API_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getEmployeeByUuidWithoutCredentialsReturnsUnauthorized() throws Exception {
        mockMvc.perform(get(EMPLOYEE_API_PATH + "/00000000-0000-0000-0000-000000000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createEmployeeWithoutCredentialsReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(EMPLOYEE_API_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }
}
