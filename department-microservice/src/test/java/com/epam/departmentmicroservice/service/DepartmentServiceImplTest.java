package com.epam.departmentmicroservice.service;

import com.epam.departmentmicroservice.dto.DepartmentDTO;
import com.epam.departmentmicroservice.dto.EmployeeDTO;
import com.epam.departmentmicroservice.entity.Department;
import com.epam.departmentmicroservice.exception.DepartmentNotFoundException;
import com.epam.departmentmicroservice.feignclient.EmployeeClient;
import com.epam.departmentmicroservice.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.epam.departmentmicroservice.example.DepartmentExample.createDepartment;
import static com.epam.departmentmicroservice.example.DepartmentExample.createDepartmentTo;
import static com.epam.departmentmicroservice.example.EmployeeExample.createEmployeeTo;
import static com.epam.departmentmicroservice.example.EmployeeExample.createManagerTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EmployeeClient employeeClient;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private long testId = 1L;

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

        Department department = createDepartment(testId, "World Hello");
        EmployeeDTO manager = createManagerTo(testId);
        DepartmentDTO expectedDepartmentDTO = new DepartmentDTO(department, manager, 5L);

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(department));
        when(employeeClient.findManagerByDepartmentId(testId)).thenReturn(manager);
        when(employeeClient.countEmployeesByDepartmentId(testId)).thenReturn(5L);

        DepartmentDTO foundDepartmentDTO = departmentService.findById(testId);

        assertNotNull(foundDepartmentDTO);
        assertEquals(expectedDepartmentDTO, foundDepartmentDTO);
    }

    @Test
    void renameDepartment() {
    }

    @Test
    void findNameById() {
    }

    @Test
    void save() {

        Department department = createDepartment(testId, "World Hello");
        EmployeeDTO manager = createManagerTo(testId);
        DepartmentDTO expectedDepartmentDTO = new DepartmentDTO(department, manager, 5L);

        when(departmentRepository.save(any()))
                .thenReturn(department);
        when(employeeClient.findManagerByDepartmentId(testId)).thenReturn(manager);
        when(employeeClient.countEmployeesByDepartmentId(testId)).thenReturn(5L);

        DepartmentDTO savedDepartmentDTO = departmentService.save(createDepartmentTo(1L, "World Hello"));

        assertNotNull(savedDepartmentDTO);
        assertEquals(expectedDepartmentDTO, savedDepartmentDTO);
    }

    @Test
    void deleteById() {
    }

    @Test
    void findDepartmentByName() {

        String testName = "World Hello";
        Department department = createDepartment(1L, testName);
        EmployeeDTO manager = createManagerTo(testId);
        DepartmentDTO expectedDepartmentDTO = new DepartmentDTO(department, manager, 5L);

        when(departmentRepository.findDepartmentByNameEquals(eq(testName)))
                .thenReturn(department);
        when(employeeClient.findManagerByDepartmentId(testId)).thenReturn(manager);
        when(employeeClient.countEmployeesByDepartmentId(testId)).thenReturn(5L);

        DepartmentDTO foundDepartmentDTO = departmentService.findDepartmentByName(testName);

        assertNotNull(foundDepartmentDTO);
        assertEquals(expectedDepartmentDTO, foundDepartmentDTO);

    }

    @Test
    void findSubDepartmentsByParentId() {

        Department parent = createDepartment(1L, "World Hello");
        Department sub1 = createDepartment(2L, "World Privet");
        Department sub2 = createDepartment(3L, "World Hola");

        parent.getSubDepartments().add(sub1);
        parent.getSubDepartments().add(sub2);

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(parent));

        Set<DepartmentDTO> foundResult = departmentService.findSubDepartmentsByParentId(1L);

        assertEquals(2, foundResult.size());
    }

    @Test
    void findAllSubDepartmentsByParentId() {

        Department parent = createDepartment(1L, "World Hello");
        Department sub1 = createDepartment(2L, "World Privet");
        Department sub2 = createDepartment(3L, "World Hola");

        parent.getSubDepartments().add(sub1);
        sub1.getSubDepartments().add(sub2);

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(parent));

        Set<DepartmentDTO> foundResult = departmentService.findAllSubDepartmentsByParentId(1L);

        assertEquals(2, foundResult.size());
    }

    @Test
    void moveDepartment() {
    }

    @Test
    void findAllParentDepartmentsById() {

        Department parent1 = createDepartment(1L, "World Hello");
        Department parent2 = createDepartment(2L, "World Privet");
        Department parent3 = createDepartment(3L, "World Go");
        Department sub = createDepartment(4L, "World Hola");

        sub.setParentDepartment(parent1);
        parent3.setParentDepartment(parent1);
        parent1.setParentDepartment(parent2);

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(sub));

        Set<DepartmentDTO> foundResult = departmentService.findAllParentDepartmentsById(4L);

        assertEquals(2, foundResult.size());
    }

    @Test
    void sumEmployeeSalaryById() {

        Department department = createDepartment(testId, "World Hello");

        EmployeeDTO employeeDTO = createEmployeeTo(testId);

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(department));
        when(employeeClient.findEmployeesByDepartmentId(testId))
                .thenReturn(Collections.singletonList(employeeDTO));

        Integer foundFund = departmentService.sumEmployeeSalaryById(testId);

        Integer expectedFund = employeeDTO.getSalary();

        assertEquals(expectedFund, foundFund);
    }

    @Test
    void findDepartmentIdByDepartmentName() {

    }

    @Test
    void transferEmployeesFromIdToNewId() {

    }

    @Test
    void throwDepartmentNotFoundException() {

        when(departmentRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(departmentRepository.findDepartmentByNameEquals(any()))
                .thenReturn(null);

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.findById(1L));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.deleteById(1L));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.findDepartmentByName("test"));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.findSubDepartmentsByParentId(1L));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.findAllSubDepartmentsByParentId(1L));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.findAllParentDepartmentsById(1L));

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.sumEmployeeSalaryById(1L));

//        assertThrows(DepartmentNotFoundException.class,
//                () -> departmentService.transferEmployeesFromIdToNewId(1L, 2L));
    }
}