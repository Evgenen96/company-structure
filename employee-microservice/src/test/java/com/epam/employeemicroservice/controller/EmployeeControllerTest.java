package com.epam.employeemicroservice.controller;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

import static com.epam.employeemicroservice.example.EmployeeExample.createEmployeeTo;
import static com.epam.employeemicroservice.example.EmployeeExample.createManagerTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private RequestAttributes attributes;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(attributes);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void findAll() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeController.findAll()).thenReturn(Collections.singletonList(employeeDTO));
        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(employeeDTO))))
                .andExpect(status().isOk());
    }

    @Test
    void addEmployee() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(0L);
        EmployeeDTO managerTo = createManagerTo(1L);
        Mockito.when(employeeService.findManagerByDepartmentName(any())).thenReturn(managerTo);
        Mockito.when(employeeService.save(any())).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
    }

    @Test
    void updateEmployee() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        EmployeeDTO managerTo = createManagerTo(2L);

        Mockito.when(employeeService.findManagerByDepartmentName(any())).thenReturn(managerTo);
        Mockito.when(employeeService.update(any())).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
    }

    @Test
    void dismissEmployee() throws Exception {

        LocalDate dismissalDate = LocalDate.of(2020, Month.JUNE, 11);
        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        employeeDTO.setDismissalDate(dismissalDate);

        Mockito.when(employeeService.dismissEmployee(any(), eq(dismissalDate.toString())))
                .thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees/{id}/dismiss", 1)
                .param("date", dismissalDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dismissalDate").value(dismissalDate.toString()));

        assertThat(employeeDTO.getDismissalDate(), is(dismissalDate));
    }

    @Test
    void getEmployee() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeService.findById(any())).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
    }

    @Test
    void findEmployeesByDepartmentId() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeService.findActualEmployeesByDepartmentId(any()))
                .thenReturn(Collections.singletonList(employeeDTO));

        mockMvc.perform(get("/api/employees/department/{departmentId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(employeeDTO))));
    }

    @Test
    void countEmployeesByDepartmentId() throws Exception {

        Mockito.when(employeeService.countActualEmployeesByDepartmentId(eq(1L)))
                .thenReturn(5L);

        mockMvc.perform(get("/api/employees/department/{departmentId}/count", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(5L)));
    }

    @Test
    void findManagerByDepartmentId() throws Exception {

        EmployeeDTO employeeDTO = createManagerTo(1L);
        Mockito.when(employeeService.findManagerByDepartmentId(eq(1L))).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/department/{departmentId}/manager", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
    }

    @Test
    void findManagerByDepartmentName() throws Exception {

        EmployeeDTO employeeDTO = createManagerTo(1L);
        Mockito.when(employeeService.findManagerByDepartmentName(eq("World"))).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/department/name/{departmentName}/manager", "World"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
    }

    @Test
    void transferEmployee() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeService.transferEmployee(eq(1L), eq(2L))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees/{id}/transfer/{targetId}", 1, 2))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(jsonPath("$.departmentName").value(employeeDTO.getDepartmentName()));
    }

    @Test
    void findManager() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(2L);
        Mockito.when(employeeService.findManagerOfEmployeeByEmployeeId(eq(1L))).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/{id}/manager", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDTO)));
}

    @Test
    void findFilteredEmployees_LastNameAndFirstName() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeService.findFilteredEmployees(any(), any(), any()))
                .thenReturn(Collections.singletonList(employeeDTO));

        mockMvc.perform(get("/api/employees/find")
                .param("lastName", employeeDTO.getLastName())
                .param("firstName", employeeDTO.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(employeeDTO))));
    }

    @Test
    void findFilteredEmployees_JobTitle() throws Exception {

        EmployeeDTO employeeDTO = createEmployeeTo(1L);
        Mockito.when(employeeService.findFilteredEmployees(any(), any(), any()))
                .thenReturn(Collections.singletonList(employeeDTO));

        mockMvc.perform(get("/api/employees/find")
                .param("lastName", employeeDTO.getJobTitle()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(employeeDTO))));
    }
}