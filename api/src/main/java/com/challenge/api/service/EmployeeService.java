package com.challenge.api.service;

import com.challenge.api.dto.EmployeeCreateRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImplementation;
import jakarta.annotation.Nonnull;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service layer for Employee operations.
 * @implNote Uses mock in-memory storage.
 */
@Service
public class EmployeeService {

    private final Map<UUID, Employee> employees;

    /** Initializes the service with a mock employee repository. */
    public EmployeeService() {
        employees = new ConcurrentHashMap<>();
    }

    /**
     * @implNote Returns mock Employee objects from an in-memory map of Employees.
     * @return One or more Employees.
     */
    public List<Employee> getAllEmployees() {
        return List.copyOf(employees.values());
    }

    /**
     * Returns the employee identified by the supplied UUID.
     *
     * @param uuid Employee's UUID
     * @return Matching employee
     * @throws ResponseStatusException with status {@code 404 Not Found} if no
     *     employee has the supplied UUID
     */
    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = employees.get(uuid);

        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + uuid);
        }

        return employee;
    }
    /**
     * Creates and stores a new employee.
     *
     * @param requestBody validated employee creation request body
     * @return the created employee
     * @throws ResponseStatusException with status {@code 400 Bad Request} if the
     *  termination date is earlier than the hire date
     */
    public Employee createEmployee(@Nonnull EmployeeCreateRequest requestBody) {
        Instant terminationDate = requestBody.contractTerminationDate();

        if (terminationDate != null && terminationDate.isBefore(requestBody.contractHireDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Contract termination date cannot be before hire date");
        }

        UUID id = UUID.randomUUID();
        Employee employee = new EmployeeImplementation(
                id,
                requestBody.firstName(),
                requestBody.lastName(),
                requestBody.salary(),
                requestBody.age(),
                requestBody.jobTitle(),
                requestBody.email(),
                requestBody.contractHireDate());
        employee.setContractTerminationDate(terminationDate);
        employees.put(id, employee);
        return employee;
    }
}
