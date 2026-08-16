package com.challenge.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Instant;

/**
 * DTO that contains the validated data required to create an employee.
 *
 * @param firstName the employee's non-blank first name
 * @param lastName the employee's non-blank last name
 * @param salary the employee's non-negative salary
 * @param age the employee's positive age
 * @param jobTitle the employee's non-blank job title
 * @param email the employee's valid email address
 * @param contractHireDate the required contract hire date
 * @param contractTerminationDate the optional contract termination date, or
 *     {@code null} for an active contract
 */
public record EmployeeCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull @PositiveOrZero Integer salary,
        @NotNull @Positive Integer age,
        @NotBlank String jobTitle,
        @Email @NotBlank String email,
        @NotNull Instant contractHireDate,
        @Nullable Instant contractTerminationDate) {}
