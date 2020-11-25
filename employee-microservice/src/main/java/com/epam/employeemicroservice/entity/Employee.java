package com.epam.employeemicroservice.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;

    @Basic
    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "hiring_date")
    private LocalDate hiringDate;

    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;

    @ManyToOne()
    @JoinColumn(name = "job_title_id")
    private JobTitle jobTitle;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "manager")
    private Boolean isManager;

    public Employee() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }

    public LocalDate getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(LocalDate dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean head) {
        isManager = head;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + employeeId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return employeeId.equals(employee.employeeId) &&
                lastName.equals(employee.lastName) &&
                firstName.equals(employee.firstName) &&
                Objects.equals(patronymic, employee.patronymic) &&
                gender == employee.gender &&
                dateOfBirth.equals(employee.dateOfBirth) &&
                phone.equals(employee.phone) &&
                email.equals(employee.email) &&
                hiringDate.equals(employee.hiringDate) &&
                Objects.equals(dismissalDate, employee.dismissalDate) &&
                Objects.deepEquals(jobTitle, employee.jobTitle) &&
                Objects.equals(departmentId, employee.departmentId) &&
                salary.equals(employee.salary) &&
                isManager.equals(employee.isManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, lastName, firstName, dateOfBirth);
    }


    public enum Gender {
        MALE("Мужской"),
        FEMALE("Женский");

        private String type;

        Gender(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static Gender getGender(String name) {
            if (name.equalsIgnoreCase("Мужской")) {
                return MALE;
            } else {
                return FEMALE;
            }
        }
    }

    @Converter
    private static class GenderConverter implements AttributeConverter<Gender, String> {

        @Override
        public String convertToDatabaseColumn(Gender gender) {
            return gender.type;
        }

        @Override
        public Gender convertToEntityAttribute(String s) {
            if (s.equalsIgnoreCase(Gender.MALE.getType())) {
                return Gender.MALE;
            } else return Gender.FEMALE;
        }
    }
}
