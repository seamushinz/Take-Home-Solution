package com.challenge.api.dto;

import jakarta.annotation.Nullable;
import java.time.Instant;

public record EmployeeCreateRequest(
        String firstName,
        String lastName,
        Integer salary,
        Integer age,
        String jobTitle,
        String email,
        Instant contractHireDate,
        @Nullable Instant contractTerminationDate) {}
