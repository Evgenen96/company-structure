package com.epam.departmentmicroservice.controller;


import com.epam.departmentmicroservice.dto.DepartmentDTO;
import com.epam.departmentmicroservice.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.junit.jupiter.api.Assertions;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.epam.departmentmicroservice.example.DepartmentExample.createDepartmentTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private RequestAttributes attributes;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(attributes);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void findAll() throws Exception {

        DepartmentDTO departmentDTO = createDepartmentTo(1L);
        Mockito.when(departmentService.findAll()).thenReturn(Collections.singletonList(departmentDTO));
        mockMvc.perform(get("/api/departments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(departmentDTO))))
                .andExpect(status().isOk());
    }

    @Test
    void addDepartment() throws Exception {

        DepartmentDTO departmentDTO = createDepartmentTo(0L);
        Mockito.when(departmentService.save(any())).thenReturn(departmentDTO);

        mockMvc.perform(post("/api/departments")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(departmentDTO)));

    }

    @Test
    void renameDepartment() throws Exception {

        DepartmentDTO departmentDTO = createDepartmentTo(1L);
        departmentDTO.setName("World_world");
        Mockito.when(departmentService.renameDepartment(any(), any())).thenReturn(departmentDTO);


        mockMvc.perform(put("/api/departments/{id}/rename/{name}", 1, "World_world"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("World_world"));
    }

    @Test
    void removeDepartment() throws Exception {

        MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charsets.ISO_8859_1);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(content().string("The department is successfully deleted"));

    }

    @Test
    void getDepartment() throws Exception {

        DepartmentDTO departmentDTO = createDepartmentTo(1L);
        Mockito.when(departmentService.findById(any())).thenReturn(departmentDTO);

        mockMvc.perform(get("/api/departments/{id}", 1))
                .andExpect(content().json(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void findSubDepartments() throws Exception {

        DepartmentDTO departmentDTO1 = createDepartmentTo(2L);
        DepartmentDTO departmentDTO2 = createDepartmentTo(3L);
        Set<DepartmentDTO> departmentsTo = new HashSet<>();
        departmentsTo.add(departmentDTO1);
        departmentsTo.add(departmentDTO2);

        Mockito.when(departmentService.findSubDepartmentsByParentId(any())).thenReturn(departmentsTo);
        mockMvc.perform(get("/api/departments/{id}/sub", 1))
                .andExpect(content().json(objectMapper.writeValueAsString(departmentsTo)))
                .andExpect(status().isOk());
    }

    @Test
    void findAllSubDepartments() throws Exception {

        DepartmentDTO departmentDTO1 = createDepartmentTo(2L);
        DepartmentDTO departmentDTO2 = createDepartmentTo(3L);
        Set<DepartmentDTO> departmentsTo = new HashSet<>();
        departmentsTo.add(departmentDTO1);
        departmentsTo.add(departmentDTO2);

        Mockito.when(departmentService.findAllSubDepartmentsByParentId(any())).thenReturn(departmentsTo);
        mockMvc.perform(get("/api/departments/{id}/sub/all", 1))
                .andExpect(content().json(objectMapper.writeValueAsString(departmentsTo)))
                .andExpect(status().isOk());
    }

    @Test
    void moveDepartment() throws Exception {

        DepartmentDTO departmentDTO = createDepartmentTo(1L);
        Mockito.when(departmentService.findById(any())).thenReturn(departmentDTO);

        mockMvc.perform(put("/api/departments/{id}/move/{targetId}", 1, 2))
                .andExpect(status().isOk());
    }

    @Test
    void findParentDepartments() throws Exception {

        DepartmentDTO departmentDTO1 = createDepartmentTo(2L);
        DepartmentDTO departmentDTO2 = createDepartmentTo(3L);
        Set<DepartmentDTO> departmentsTo = new HashSet<>();
        departmentsTo.add(departmentDTO1);
        departmentsTo.add(departmentDTO2);

        Mockito.when(departmentService.findAllParentDepartmentsById(any())).thenReturn(departmentsTo);
        mockMvc.perform(get("/api/departments/{id}/parents", 1))
                .andExpect(content().json(objectMapper.writeValueAsString(departmentsTo)))
                .andExpect(status().isOk());
    }

    @Test
    void findDepartmentIdByName() throws Exception {

        Mockito.when(departmentService.findDepartmentIdByDepartmentName(any())).thenReturn(1L);

        mockMvc.perform(get("/api/departments/name/{name}/id", "World Hello"))
                .andExpect(content().string(String.valueOf(1L)))
                .andExpect(status().isOk());
    }

    @Test
    void findDepartmentNameByDepartmentId() throws Exception {

        Mockito.when(departmentService.findNameById(any())).thenReturn("World Hello");

        mockMvc.perform(get("/api/departments/{departmentId}/name/", 1))
                .andExpect(content().string("World Hello"))
                .andExpect(status().isOk());
    }

    @Test
    void findDepartmentFund() throws Exception {

        Mockito.when(departmentService.sumEmployeeSalaryById(any())).thenReturn(100000);

        mockMvc.perform(get("/api/departments/{id}/fund", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("100000"));

    }

    @Test
    void transferEmployees() throws Exception {

        MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charsets.ISO_8859_1);

        DepartmentDTO departmentDTO = createDepartmentTo(2L);
        Mockito.when(departmentService.transferEmployeesFromIdToNewId(any(), any())).thenReturn(departmentDTO);

        mockMvc.perform(put("/api/departments/{id}/employees/transfer/{targetId}", 1, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(content().string("All the department employees are successfully transferred!"));
    }

    @Test
    void transferEmployees_ExceptionThrown() {

        DepartmentDTO departmentDTO = createDepartmentTo(2L);
        Mockito.when(departmentService.transferEmployeesFromIdToNewId(any(), any())).thenReturn(departmentDTO);

        Assertions.assertThrows(Exception.class, () ->
                mockMvc.perform(put("/api/departments/{id}/employees/transfer/{targetId}", 1, 1)));
    }
}