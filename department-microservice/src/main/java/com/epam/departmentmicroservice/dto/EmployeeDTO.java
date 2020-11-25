package com.epam.departmentmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The {@code EmployeeDTO} class represents DTO pattern that
 * processes and stores necessary data from {@code Employee }
 * entity class.
 */
public class EmployeeDTO {

    private Long employeeId;

    @NotNull
    @Pattern(regexp = "^[а-яА-Я-]*", message = "Last name should consist of russian chars and '-'")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[а-яА-Я-]*", message = "First name should consist of russian chars and '-'")
    private String firstName;

    @Pattern(regexp = "^[а-яА-Я-]*", message = "Patronymic should consist of russian chars and '-'")
    private String patronymic;

    @NotNull
    private String gender;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    @Pattern(regexp = "^(([+]?)[-()0-9 ]{10,15}$)", message = "Phone should be valid")
    private String phone;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd")

    private LocalDate hiringDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd")
    private LocalDate dismissalDate;

    @NotNull
    private String jobTitle;

    @NotNull
    private String department;

    @NotNull
    @PositiveOrZero
    private Integer salary;

    @NotNull
    private Boolean manager;

    public EmployeeDTO() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    @ApiModelProperty(example = "1")
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    @ApiModelProperty(example = "Иванов")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @ApiModelProperty(example = "Иван")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @ApiModelProperty(example = "Иванович")
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getGender() {
        return gender;
    }

    @ApiModelProperty(example = "Мужской")
    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @ApiModelProperty(example = "1995-01-01")
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    @ApiModelProperty(example = "89874548144")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    @ApiModelProperty(example = "example@epam.com")
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHiringDate() {
        return hiringDate;
    }

    @ApiModelProperty(example = "2010-01-01")
    public void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }

    public LocalDate getDismissalDate() {
        return dismissalDate;
    }

    @ApiModelProperty(example = "null")
    public void setDismissalDate(LocalDate dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @ApiModelProperty(example = "Solution Architect")
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    @ApiModelProperty(example = "Andrew Cordeiro")
    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSalary() {
        return salary;
    }

    @ApiModelProperty(example = "35000")
    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Boolean getManager() {
        return manager;
    }

    @ApiModelProperty(
            notes = "value 'true' means the employee is manager of his department",
            example = "false")
    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDTO)) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(employeeId, that.employeeId) &&
                lastName.equals(that.lastName) &&
                firstName.equals(that.firstName) &&
                Objects.equals(patronymic, that.patronymic) &&
                gender.equals(that.gender) &&
                dateOfBirth.equals(that.dateOfBirth) &&
                phone.equals(that.phone) &&
                email.equals(that.email) &&
                hiringDate.equals(that.hiringDate) &&
                Objects.equals(dismissalDate, that.dismissalDate) &&
                jobTitle.equals(that.jobTitle) &&
                department.equals(that.department) &&
                Objects.equals(salary, that.salary) &&
                Objects.equals(manager, that.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, lastName, firstName, patronymic, gender, dateOfBirth, phone, email, hiringDate, dismissalDate, jobTitle, department, salary, manager);
    }
}
