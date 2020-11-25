package com.epam.departmentmicroservice.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "department", uniqueConstraints = @UniqueConstraint(
        name = "uk_department_name", columnNames = {"name"}))
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne()
    @JoinColumn(name = "department_parent_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment")
    private Set<Department> subDepartments = new HashSet<>();

    public Department() {
    }

    public Department(String name, LocalDate creationDate, Department parentDepartment) {
        this.name = name;
        this.creationDate = creationDate;
        this.parentDepartment = parentDepartment;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Set<Department> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(Set<Department> subDepartments) {
        this.subDepartments = subDepartments;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", parentDepartment=" + parentDepartment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name, creationDate);
    }
}