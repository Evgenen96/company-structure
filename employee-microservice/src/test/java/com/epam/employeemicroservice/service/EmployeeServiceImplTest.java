package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.feignclient.DepartmentClient;
import com.epam.employeemicroservice.repository.EmployeeRepository;
import com.epam.employeemicroservice.repository.JobTitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.epam.employeemicroservice.example.EmployeeExample.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentClient departmentClient;
    @Mock
    private JobTitleRepository jobTitleRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private long testId;

    @BeforeEach
    void setUp() {
        testId = 1L;
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {

        Employee employee = createEmployee(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, "World");

        when(employeeRepository.findById(eq(testId)))
                .thenReturn(Optional.of(employee));
        when(departmentClient.findDepartmentNameByDepartmentId(eq(employee.getDepartmentId())))
                .thenReturn("World");

        EmployeeDTO foundEmployeeDTO = employeeService.findById(testId);

        assertNotNull(foundEmployeeDTO);
        assertEquals(expectedEmployeeDTO, foundEmployeeDTO);
    }

    @Test
    void save() {

        Employee employee = createEmployee(testId);
        EmployeeDTO employeeDTO = createEmployeeTo(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, "World Hello");

        when(employeeRepository.save(any()))
                .thenReturn(employee);
        when(jobTitleRepository.findJobTitleByTitle(
                eq(employee.getJobTitle().getTitle())))
                .thenReturn(employee.getJobTitle());
        when(departmentClient.findDepartmentIdByDepartmentName(eq(employeeDTO.getDepartmentName())))
                .thenReturn(1L);

        EmployeeDTO savedEmployeeDTO = employeeService.save(employeeDTO);

        assertNotNull(savedEmployeeDTO);
        assertEquals(expectedEmployeeDTO, savedEmployeeDTO);
    }

    @Test
    void update() {

        Employee employee = createEmployee(testId);
        EmployeeDTO employeeDTO = createEmployeeTo(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, "World Hello");

        when(employeeRepository.save(any()))
                .thenReturn(employee);
        when(employeeRepository.findById(
                eq(employee.getEmployeeId())))
                .thenReturn(Optional.of(employee));
        when(jobTitleRepository.findJobTitleByTitle(
                eq(employee.getJobTitle().getTitle())))
                .thenReturn(employee.getJobTitle());
        when(departmentClient.findDepartmentIdByDepartmentName(eq(employeeDTO.getDepartmentName())))
                .thenReturn(1L);

        EmployeeDTO updatedEmployeeDTO = employeeService.update(employeeDTO);

        assertNotNull(updatedEmployeeDTO);
        assertEquals(expectedEmployeeDTO, updatedEmployeeDTO);
    }

    @Test
    void dismissEmployee() {

    }

    @Test
    void deleteById() {
    }

    @Test
    void findActualEmployeesByDepartmentId() {
    }

    @Test
    void countActualEmployeesByDepartmentId() {
    }

    @Test
    void findManagerByDepartmentId() {
    }

    @Test
    void findManagerByDepartmentName() {
    }

    @Test
    void findManagerOfEmployeeByEmployeeId() {

        Employee employee = createEmployee(testId);
        Employee manager = createManager(2L);


        EmployeeDTO expectedResult = new EmployeeDTO(manager, "World Hello");
        EmployeeDTO toFindBy = new EmployeeDTO(employee, "World Hello");

        when(employeeRepository.findById(eq(testId)))
                .thenReturn(Optional.of(employee));
        when(departmentClient.findDepartmentNameByDepartmentId(eq(employee.getDepartmentId())))
                .thenReturn("World Hello");
        when(departmentClient.findDepartmentIdByDepartmentName(eq(toFindBy.getDepartmentName())))
                .thenReturn(testId);
        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(manager);

        EmployeeDTO actualResult = employeeService.findManagerOfEmployeeByEmployeeId(1L);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void transferEmployee() {


    }
}