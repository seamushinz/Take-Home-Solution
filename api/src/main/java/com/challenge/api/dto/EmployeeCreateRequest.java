package com.challenge.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Instant;

/**
 * DTO for new Employee creation requests
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
