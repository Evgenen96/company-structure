package com.epam.employeemicroservice.controller;

import com.epam.employeemicroservice.exception.CannotExecuteException;
import com.epam.employeemicroservice.exception.InvalidParametersException;
import com.epam.employeemicroservice.exception.ResourceNotFoundException;
import com.epam.employeemicroservice.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class RestExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new RestExceptionHandler()).build();
    }

    @Test
    void handleResourceNotFoundException() throws Exception {

        String errorMessage = "test not found exception";

        when(employeeService.findById(any()))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(get("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void handleInvalidParametersException() throws Exception {

        String errorMessage = "test parameter exception";

        when(employeeService.findById(any()))
                .thenThrow(new InvalidParametersException(errorMessage));

        mockMvc.perform(get("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void handleException() throws Exception {

        String errorMessage = "a exception";

        when(employeeService.findById(any()))
                .thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(get("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Oops, something is completely wrong :("));
    }

    @Test
    void handleDBConnectionException() throws Exception {

        String errorMessage = "test hibernate exception";

        when(employeeService.findById(any()))
                .thenThrow(new CannotExecuteException(errorMessage));

        mockMvc.perform(get("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

}
