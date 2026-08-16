package com.challenge.api.controller;

import com.challenge.api.dto.EmployeeCreateRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** REST endpoints for retrieving and creating employees. */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee models as necessary.
     * @return One or more Employees.
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee model as necessary.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    @GetMapping("/{uuid}")
    public Employee getEmployeeByUuid(@PathVariable UUID uuid) {
        return employeeService.getEmployeeByUuid(uuid);
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer.
     * @param requestBody Validated request body containing Employee details
     * @return Newly created Employee
     * @see EmployeeCreateRequest
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody EmployeeCreateRequest requestBody) {
        return employeeService.createEmployee(requestBody);
    }
}
