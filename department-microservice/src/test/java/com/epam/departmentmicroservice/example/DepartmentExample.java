package com.epam.departmentmicroservice.example;

import com.epam.departmentmicroservice.dto.DepartmentDTO;
import com.epam.departmentmicroservice.entity.Department;

import java.time.LocalDate;
import java.time.Month;

public class DepartmentExample {

    private DepartmentExample() {

    }

    public static Department createDepartment(long id, String name) {

        Department department = new Department();
        department.setDepartmentId(id);
        department.setName(name);
        department.setCreationDate(LocalDate.of(2020, Month.OCTOBER, 10));

        return department;
    }

    public static DepartmentDTO createDepartmentTo(Long id) {

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(id);
        departmentDTO.setName("World Hello");
        departmentDTO.setCreationDate(LocalDate.of(2010, Month.OCTOBER, 10));
        departmentDTO.setParentDepartmentId(null);
        departmentDTO.setEmployeesAmount(8L);
        departmentDTO.setManager("Ilia Manager");

        return departmentDTO;
    }

    public static DepartmentDTO createDepartmentTo(Long id, String name) {

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(id);
        departmentDTO.setName(name);
        departmentDTO.setCreationDate(LocalDate.of(2010, Month.OCTOBER, 10));
        departmentDTO.setParentDepartmentId(null);
        departmentDTO.setEmployeesAmount(8L);
        departmentDTO.setManager("Ilia Manager");

        return departmentDTO;
    }
}
