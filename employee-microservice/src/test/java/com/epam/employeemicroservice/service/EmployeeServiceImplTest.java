package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.exception.EmployeeNotFoundException;
import com.epam.employeemicroservice.repository.EmployeeRepository;
import com.epam.employeemicroservice.repository.JobTitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.employeemicroservice.example.EmployeeExample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentResolverService departmentResolverService;
    @Mock
    private JobTitleRepository jobTitleRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private final long testId = 1L;
    private final String departmentTestName = "World Hello";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {

        Employee employee = createEmployee(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, departmentTestName);

        when(employeeRepository.findById(eq(testId)))
                .thenReturn(Optional.of(employee));
        when(departmentResolverService.findDepartmentNameByDepartmentId(eq(employee.getDepartmentId())))
                .thenReturn(departmentTestName);

        EmployeeDTO foundEmployeeDTO = employeeService.findById(testId);

        assertNotNull(foundEmployeeDTO);
        assertEquals(expectedEmployeeDTO, foundEmployeeDTO);
    }

    @Test
    void save() {

        Employee employee = createEmployee(testId);
        EmployeeDTO employeeDTO = createEmployeeTo(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, departmentTestName);

        when(employeeRepository.save(any()))
                .thenReturn(employee);
        when(jobTitleRepository.findJobTitleByTitle(
                eq(employee.getJobTitle().getTitle())))
                .thenReturn(employee.getJobTitle());
        when(departmentResolverService.findDepartmentIdByDepartmentName(eq(employeeDTO.getDepartmentName())))
                .thenReturn(1L);

        EmployeeDTO savedEmployeeDTO = employeeService.save(employeeDTO);

        assertNotNull(savedEmployeeDTO);
        assertEquals(expectedEmployeeDTO, savedEmployeeDTO);
    }

    @Test
    void update() {

        Employee employee = createEmployee(testId);
        EmployeeDTO employeeDTO = createEmployeeTo(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, departmentTestName);

        when(employeeRepository.save(any()))
                .thenReturn(employee);
        when(employeeRepository.findById(
                eq(employee.getEmployeeId())))
                .thenReturn(Optional.of(employee));
        when(jobTitleRepository.findJobTitleByTitle(
                eq(employee.getJobTitle().getTitle())))
                .thenReturn(employee.getJobTitle());
        when(departmentResolverService.findDepartmentIdByDepartmentName(eq(employeeDTO.getDepartmentName())))
                .thenReturn(1L);

        EmployeeDTO updatedEmployeeDTO = employeeService.update(employeeDTO);

        assertNotNull(updatedEmployeeDTO);
        assertEquals(expectedEmployeeDTO, updatedEmployeeDTO);
    }

    @Test
    void dismissEmployee() {

        LocalDate dismissalDate = LocalDate.of(2020, Month.NOVEMBER, 26);

        Employee employee = createEmployee(testId);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(employee, departmentTestName);
        expectedEmployeeDTO.setDismissalDate(dismissalDate);

        when(employeeRepository.findById(
                eq(employee.getEmployeeId())))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any()))
                .thenReturn(employee);

        EmployeeDTO dismissedEmployeeDTO = employeeService.dismissEmployee(testId, dismissalDate.toString());

        assertNotNull(dismissedEmployeeDTO);
        assertEquals(expectedEmployeeDTO.getDismissalDate(), dismissedEmployeeDTO.getDismissalDate());
    }

    @Test
    void findActualEmployeesByDepartmentId() {

        when(employeeRepository.findActualEmployeesByDepartmentId(eq(testId)))
                .thenReturn(Collections.singletonList(createEmployee(testId)));
        when(departmentResolverService.findDepartmentNameByDepartmentId(eq(testId)))
                .thenReturn(departmentTestName);

        List<EmployeeDTO> employeeDTOS = employeeService.findActualEmployeesByDepartmentId(testId);

        assertEquals(1, employeeDTOS.size());
    }

    @Test
    void countActualEmployeesByDepartmentId() {

        Long expectedAmount = 10L;
        when(employeeRepository.countActualEmployeesByDepartmentId(eq(testId)))
                .thenReturn(expectedAmount);

        Long gottenAmount = employeeService.countActualEmployeesByDepartmentId(testId);

        assertEquals(expectedAmount, gottenAmount);
    }

    @Test
    void findManagerByDepartmentId() {

        Employee manager = createManager(testId);
        EmployeeDTO expectedManager = new EmployeeDTO(manager, departmentTestName);

        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(manager);
        when(departmentResolverService.findDepartmentNameByDepartmentId(eq(testId)))
                .thenReturn(departmentTestName);

        EmployeeDTO gottenManager = employeeService.findManagerByDepartmentId(testId);

        assertNotNull(gottenManager);
        assertEquals(expectedManager, gottenManager);
    }

    @Test
    void findManagerByDepartmentName() {

        Employee manager = createManager(testId);
        EmployeeDTO expectedManager = new EmployeeDTO(manager, departmentTestName);

        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(manager);
        when(departmentResolverService.findDepartmentIdByDepartmentName(eq(departmentTestName)))
                .thenReturn(testId);

        EmployeeDTO gottenManager = employeeService.findManagerByDepartmentName(departmentTestName);

        assertNotNull(gottenManager);
        assertEquals(expectedManager, gottenManager);
    }

    @Test
    void findManagerOfEmployeeByEmployeeId() {

        Employee employee = createEmployee(testId);
        Employee manager = createManager(2L);


        EmployeeDTO expectedResult = new EmployeeDTO(manager, departmentTestName);
        EmployeeDTO toFindBy = new EmployeeDTO(employee, departmentTestName);

        when(employeeRepository.findById(eq(testId)))
                .thenReturn(Optional.of(employee));
        when(departmentResolverService.findDepartmentNameByDepartmentId(eq(employee.getDepartmentId())))
                .thenReturn(departmentTestName);
        when(departmentResolverService.findDepartmentIdByDepartmentName(eq(toFindBy.getDepartmentName())))
                .thenReturn(testId);
        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(manager);

        EmployeeDTO actualResult = employeeService.findManagerOfEmployeeByEmployeeId(1L);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void transferEmployee() {

        Employee employee = createEmployee(testId);
        EmployeeDTO expectedEmployee = new EmployeeDTO(employee, departmentTestName);
        Employee manager = createManager(testId);

        when(employeeRepository.findById(eq(testId)))
                .thenReturn(Optional.of(employee));
        when(departmentResolverService.findDepartmentNameByDepartmentId(eq(testId)))
                .thenReturn(departmentTestName);
        when(employeeRepository.save(any()))
                .thenReturn(employee);
        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(manager);

        EmployeeDTO transferredEmployee = employeeService.transferEmployee(testId, testId);

        assertNotNull(transferredEmployee);
        assertEquals(expectedEmployee.getDepartmentName(), transferredEmployee.getDepartmentName());

    }

    @Test
    void throwEmployeeNotFoundException() {

        when(employeeRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(employeeRepository.findManagerByDepartmentId(eq(testId)))
                .thenReturn(null);

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.findById(testId));

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteById(testId));

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.update(createEmployeeTo(testId)));

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.dismissEmployee(testId, null));

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.findManagerByDepartmentId(testId));

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.transferEmployee(testId, testId));
    }
}