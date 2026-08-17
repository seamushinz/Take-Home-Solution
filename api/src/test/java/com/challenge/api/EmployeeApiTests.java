package com.challenge.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/** Uses mock credentials to simulate the webhook's externally configured HTTP Basic credentials. */
@SpringBootTest(
        properties = {"spring.security.user.name=employees-r-us", "spring.security.user.password=test-password"})
@AutoConfigureMockMvc
class EmployeeApiTests {

    private static final String EMPLOYEE_API_PATH = "/api/v1/employee";
    private static final String TEST_USERNAME = "employees-r-us";
    private static final String TEST_PASSWORD = "test-password";
    private static final String MOCK_EMPLOYEE_JSON_ONE =
            """
        {
          "firstName": "Jane",
          "lastName": "Doe",
          "salary": 75000,
          "age": 30,
          "jobTitle": "Engineer",
          "email": "jane.doe@example.com",
          "contractHireDate": "2026-01-01T00:00:00Z",
          "contractTerminationDate": null
        }
        """;
    private static final String MOCK_EMPLOYEE_JSON_SECOND =
            """
        {
          "firstName": "John",
          "lastName": "Smith",
          "salary": 90000,
          "age": 42,
          "jobTitle": "Manager",
          "email": "john.smith@example.com",
          "contractHireDate": "2024-06-15T00:00:00Z",
          "contractTerminationDate": null
        }
        """;

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_EMPLOYEE_JSON_ONE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllEmployeesWithValidCredentialsReturnsOk() throws Exception {
        mockMvc.perform(post(EMPLOYEE_API_PATH)
                        .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_EMPLOYEE_JSON_ONE))
                .andExpect(status().isCreated());

        mockMvc.perform(post(EMPLOYEE_API_PATH)
                        .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_EMPLOYEE_JSON_SECOND))
                .andExpect(status().isCreated());

        mockMvc.perform(get(EMPLOYEE_API_PATH)
                        .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].uuid").isNotEmpty())
                .andExpect(jsonPath("$[0].firstName").isNotEmpty())
                .andExpect(jsonPath("$[1].uuid").isNotEmpty())
                .andExpect(jsonPath("$[1].firstName").isNotEmpty());
    }

    @Test
    void getAllEmployeesWithInvalidCredentialsReturnsUnauthorized() throws Exception {
        mockMvc.perform(get(EMPLOYEE_API_PATH)
                        .with(httpBasic(TEST_USERNAME, "incorrect-password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMissingEmployeeWithValidCredentialsReturnsNotFound() throws Exception {
        mockMvc.perform(get(EMPLOYEE_API_PATH + "/00000000-0000-0000-0000-000000000000")
                        .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployeeWithValidCredentialsReturnsOk() throws Exception {
        mockMvc.perform(post(EMPLOYEE_API_PATH)
                        .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_EMPLOYEE_JSON_ONE))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployeeWithTerminationBeforeHireDateReturnsBadRequest() throws Exception {
        mockMvc.perform(
                        post(EMPLOYEE_API_PATH)
                                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                          "firstName": "Jane",
                                          "lastName": "Doe",
                                          "salary": 75000,
                                          "age": 30,
                                          "jobTitle": "Engineer",
                                          "email": "jane.doe@example.com",
                                          "contractHireDate": "2026-01-01T00:00:00Z",
                                          "contractTerminationDate": "2025-12-31T00:00:00Z"
                                        }
                                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createInvalidEmployeeWithValidCredentialsReturnsBadRequest() throws Exception {
        mockMvc.perform(
                        post(EMPLOYEE_API_PATH)
                                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {
                                  "firstName": "Jane",
                                  "lastName": "Doe",
                                  "salary": 75000,
                                  "age": 30,
                                  "jobTitle": "Engineer",
                                  "email": "not-an-email",
                                  "contractHireDate": "2026-01-01T00:00:00Z",
                                  "contractTerminationDate": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
