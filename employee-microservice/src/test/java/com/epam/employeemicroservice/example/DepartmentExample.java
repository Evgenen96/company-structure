package com.epam.employeemicroservice.example;

import com.epam.employeemicroservice.dto.DepartmentDTO;

import java.time.LocalDate;
import java.time.Month;

public class DepartmentExample {

    private DepartmentExample() {}

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
