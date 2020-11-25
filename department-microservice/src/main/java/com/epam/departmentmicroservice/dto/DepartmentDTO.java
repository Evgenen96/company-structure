package com.epam.departmentmicroservice.dto;

import com.epam.departmentmicroservice.entity.Department;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The {@code DepartmentDTO} class represents DTO pattern that
 * processes and stores necessary data from {@code Department}
 * entity class.
 */
public class DepartmentDTO {

    private Long departmentId;

    @NotNull
    private String name;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    /**
     * The field representing manager of the {@code Department}.
     * Can be null if manager isn't specified.
     */
    private String manager;

    /**
     * The field representing amount of employee working at department.
     * Fired employees are not counted
     */
    @PositiveOrZero
    private Long employeesAmount;

    @JsonIgnore
    private Long parentDepartmentId;

    public DepartmentDTO() {
    }

    public DepartmentDTO(Long id, String name, LocalDate creationDate, String manager, Long employeesAmount) {
        this.departmentId = id;
        this.name = name;
        this.creationDate = creationDate;
        this.manager = manager;
        this.employeesAmount = employeesAmount;
    }

    public DepartmentDTO(Department department, EmployeeDTO manager, Long employeesAmount) {

        this.departmentId = department.getDepartmentId();

        this.name = department.getName();

        this.creationDate = department.getCreationDate();

        this.manager = buildManagerStr(manager);

        this.employeesAmount = employeesAmount;
    }

    public String getName() {
        return name;
    }

    @ApiModelProperty(example = "ExampleName")
    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    @ApiModelProperty(example = "2000-01-01")
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getManager() {
        return manager;
    }

    @ApiModelProperty(notes = "automatically set", example = "null")
    public void setManager(String manager) {
        this.manager = manager;
    }

    private String buildManagerStr(EmployeeDTO manager) {

        StringBuilder sb = null;
        if (manager != null) {
            sb = new StringBuilder();
            sb.append(manager.getLastName())
                    .append(" ")
                    .append(manager.getFirstName())
                    .append(" ");
            if (manager.getPatronymic() != null) sb.append(manager.getPatronymic()).append(" ");
            sb.append("id - ").append(manager.getEmployeeId());
        }

        return sb != null ? sb.toString() : null;
    }

    public Long getEmployeesAmount() {
        return employeesAmount;
    }

    @ApiModelProperty(notes = "automatically set", example = "")
    public void setEmployeesAmount(Long employeesAmount) {
        this.employeesAmount = employeesAmount;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }

    @ApiModelProperty(example = "")
    public void setParentDepartmentId(Long parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    @ApiModelProperty(notes = "automatically set", example = "")
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentDTO)) return false;
        DepartmentDTO that = (DepartmentDTO) o;
        return departmentId.equals(that.departmentId) &&
                name.equals(that.name) &&
                creationDate.equals(that.creationDate) &&
                Objects.equals(manager, that.manager) &&
                Objects.equals(employeesAmount, that.employeesAmount) &&
                Objects.equals(parentDepartmentId, that.parentDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name, creationDate, manager, employeesAmount, parentDepartmentId);
    }
}
