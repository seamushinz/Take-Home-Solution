package com.challenge.api.model;

import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

// Implements the Employee model for mock data testing in the EmployeeService
public class EmployeeImplementation implements Employee {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private Integer salary;
    private Integer age;
    private String jobTitle;
    private String email;
    private Instant contractHireDate;
    private Instant contractTerminationDate;

    public EmployeeImplementation(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "UUID is required non-null");
    }

    public EmployeeImplementation(
            UUID uuid,
            String firstName,
            String lastName,
            Integer salary,
            Integer age,
            String jobTitle,
            String email,
            Instant contractHireDate) {
        this.uuid = Objects.requireNonNull(uuid, "UUID is required non-null");
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.age = age;
        this.jobTitle = jobTitle;
        this.email = email;
        this.contractHireDate = contractHireDate;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Set by either the Service or Data layer.
     *
     * @param uuid required non-null
     */
    @Override
    public void setUuid(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "UUID is required non-null");
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(String name) {
        this.firstName = name;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setLastName(String name) {
        this.lastName = name;
    }

    @Override
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public void setFullName(String name) {
        String[] names = name.split(" ");
        this.firstName = names[0];
        this.lastName = names[1];
    }

    @Override
    public Integer getSalary() {
        return this.salary;
    }

    @Override
    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public Integer getAge() {
        return this.age;
    }

    @Override
    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String getJobTitle() {
        return this.jobTitle;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Instant getContractHireDate() {
        return this.contractHireDate;
    }

    @Override
    public void setContractHireDate(Instant date) {
        this.contractHireDate = date;
    }

    /**
     * Returns the date the employee's contract was terminated, or null if the employee has not been terminated.
     * @return the termination date, or null if not terminated
     */
    @Override
    @Nullable public Instant getContractTerminationDate() {
        return this.contractTerminationDate;
    }

    @Override
    public void setContractTerminationDate(Instant date) {
        this.contractTerminationDate = date;
    }
}
