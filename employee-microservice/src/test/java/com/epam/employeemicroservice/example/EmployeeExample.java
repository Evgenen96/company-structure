package com.epam.employeemicroservice.example;

import com.epam.employeemicroservice.dto.DepartmentDTO;
import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.entity.JobTitle;

import java.time.LocalDate;
import java.time.Month;

public class EmployeeExample {

    private EmployeeExample() {}

    public static Employee createEmployee(Long id) {

        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setLastName("Никонов");
        employee.setFirstName("Марат");
        employee.setPatronymic("Сергеевич");
        employee.setDateOfBirth(LocalDate.of(1996, Month.SEPTEMBER, 12));
        employee.setGender(Employee.Gender.MALE);
        employee.setEmail("nikonovki@gmail.com");
        employee.setPhone("89868674712");
        employee.setSalary(55555);
        employee.setHiringDate(LocalDate.of(2010, Month.JUNE, 12));
        employee.setManager(false);
        employee.setJobTitle(new JobTitle("Разработчик"));
        employee.setDepartmentId(1L);

        return employee;
    }

    public static Employee createManager(Long id) {

        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setLastName("Боратов");
        employee.setFirstName("Григорий");
        employee.setPatronymic("Сергеевич");
        employee.setDateOfBirth(LocalDate.of(1986, Month.SEPTEMBER, 12));
        employee.setGender(Employee.Gender.MALE);
        employee.setEmail("sdjfnqq@gmail.com");
        employee.setPhone("89868612134");
        employee.setSalary(100000);
        employee.setHiringDate(LocalDate.of(2005, Month.JUNE, 12));
        employee.setManager(true);
        employee.setJobTitle(new JobTitle("Разработчик"));
        employee.setDepartmentId(1L);

        return employee;
    }

    public static EmployeeDTO createEmployeeTo(Long id) {

        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(id);
        employee.setLastName("Никонов");
        employee.setFirstName("Марат");
        employee.setPatronymic("Сергеевич");
        employee.setDateOfBirth(LocalDate.of(1996, Month.SEPTEMBER, 12));
        employee.setGender(Employee.Gender.MALE.getType());
        employee.setEmail("nikonovki@gmail.com");
        employee.setPhone("89868674712");
        employee.setSalary(55555);
        employee.setHiringDate(LocalDate.of(2010, Month.JUNE, 12));
        employee.setManager(false);
        employee.setDepartmentName("World Hello");
        employee.setJobTitle("Разработчик");

        return employee;
    }

    public static EmployeeDTO createManagerTo(Long id) {

        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(id);
        employee.setLastName("Боратов");
        employee.setFirstName("Григорий");
        employee.setPatronymic("Сергеевич");
        employee.setDateOfBirth(LocalDate.of(1986, Month.SEPTEMBER, 12));
        employee.setGender(Employee.Gender.MALE.getType());
        employee.setEmail("sdjfnqq@gmail.com");
        employee.setPhone("89868612134");
        employee.setSalary(100000);
        employee.setHiringDate(LocalDate.of(2005, Month.JUNE, 12));
        employee.setManager(true);
        employee.setDepartmentName("World Hello");
        employee.setJobTitle("Разработчик");

        return employee;
    }
}
