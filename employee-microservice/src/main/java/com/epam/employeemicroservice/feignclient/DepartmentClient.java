package com.epam.employeemicroservice.feignclient;

import com.epam.employeemicroservice.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Primary
@FeignClient(
        name = "department-service",
        url = "http://localhost:8082/api",
        fallbackFactory = DepartmentClientFallbackFactory.class)
public interface DepartmentClient {

    @GetMapping("/departments")
    List<DepartmentDTO> findAll();

    @GetMapping("/departments/{departmentId}")
    DepartmentDTO findById(@PathVariable Long departmentId);

    @GetMapping("/departments/{departmentId}/name")
    String findDepartmentNameByDepartmentId(@PathVariable Long departmentId);

    @GetMapping("/departments/name/{departmentName}/id")
    Long findDepartmentIdByDepartmentName(@PathVariable String departmentName);
}
