package com.challenge.api.service;

import com.challenge.api.dto.EmployeeCreateRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImplementation;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// Service layer for Employee Controller
@Service
public class EmployeeService {

    // Map of mock Employees, to be replaced with existing employee management solution repository
    private final Map<UUID, Employee> employeeRepository;

    public EmployeeService() {
        // create mock Employee list
        employeeRepository = new HashMap<>();
        Employee employee1 = new EmployeeImplementation(UUID.randomUUID());
        employee1.setFullName("John Doe");
        employee1.setEmail("john.doe@example.com");
        employeeRepository.put(employee1.getUuid(), employee1);
        Employee employee2 = new EmployeeImplementation(UUID.randomUUID());
        employee2.setFullName("George Rowan");
        employee2.setEmail("george.rowan@reliaquest.com");
        employee2.setAge(30);
        employeeRepository.put(employee2.getUuid(), employee2);
    }

    /**
     * @implNote Returns mock Employee objects from an in-memory map of Employees.
     * @return One or more Employees.
     */
    public List<Employee> getAllEmployees() {
        return List.copyOf(employeeRepository.values());
    }

    /**
     * @implNote Returns a mock Employee object from an in-memory map of Employees.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employeeRepository.get(uuid);

        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + uuid);
        }

        return employee;
    }

    /**
     * @implNote Creates a new Employee based on the provided request body.
     * @param requestBody The request body containing employee details.
     * @return The created Employee.
     */
    public Employee createEmployee(EmployeeCreateRequest requestBody) {
        // Implement logic to create a new Employee
        UUID id = UUID.randomUUID();

        // Validate request body dates
        Instant terminationDate = requestBody.contractTerminationDate();

        if (terminationDate != null && terminationDate.isBefore(requestBody.contractHireDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Contract termination date cannot be before hire date");
        }
        Employee employee = new EmployeeImplementation(
                id,
                requestBody.firstName(),
                requestBody.lastName(),
                requestBody.salary(),
                requestBody.age(),
                requestBody.jobTitle(),
                requestBody.email(),
                requestBody.contractHireDate());
        employee.setContractTerminationDate(requestBody.contractTerminationDate());
        employeeRepository.put(id, employee);
        return employee;
    }
}
