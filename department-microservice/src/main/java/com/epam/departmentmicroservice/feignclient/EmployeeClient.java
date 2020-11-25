package com.epam.departmentmicroservice.feignclient;

import com.epam.departmentmicroservice.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Primary
@FeignClient(
        name = "employee-service",
        url = "http://localhost:8081/api",
        fallbackFactory = EmployeeClientFallbackFactory.class)
public interface EmployeeClient {

    @GetMapping("/employees/department/{departmentId}")
    List<EmployeeDTO> findEmployeesByDepartmentId(@PathVariable long departmentId);

    @GetMapping("/employees/department/{departmentId}/count")
    Long countEmployeesByDepartmentId(@PathVariable long departmentId);

    @GetMapping("/employees/department/{departmentId}/manager")
    EmployeeDTO findManagerByDepartmentId(@PathVariable long departmentId);

    @PutMapping("/employees/{employeeId}/transfer/{departmentId}")
    EmployeeDTO transferEmployee(
            @PathVariable long employeeId,
            @PathVariable long departmentId);
}