package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService extends BaseService<EmployeeDTO> {

    EmployeeDTO update(EmployeeDTO employeeDTO);

    EmployeeDTO dismissEmployee(Long id, String dismissalDate);

    /**
     * Filters the employees. Two ways of use: specify the lastName
     * and the firstName or specify the job title
     * @return List of employees corresponding to the filters
     */
    List<EmployeeDTO> findFilteredEmployees(String lastName, String firstName, String jobTitle);

    List<EmployeeDTO> findActualEmployeesByDepartmentId(Long departmentId);

    Long countActualEmployeesByDepartmentId(long departmentId);

    EmployeeDTO findManagerByDepartmentId(long departmentId);

    EmployeeDTO findManagerByDepartmentName(String departmentName);

    EmployeeDTO findManagerOfEmployeeByEmployeeId(long employeeId);

    EmployeeDTO transferEmployee(long employeeId, long departmentId);
}
