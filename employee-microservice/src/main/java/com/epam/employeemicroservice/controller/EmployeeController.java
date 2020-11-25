package com.epam.employeemicroservice.controller;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.service.DepartmentSnapshotService;
import com.epam.employeemicroservice.service.EmployeeService;
import com.epam.employeemicroservice.validation.EmployeeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

    @GetMapping
    public List<EmployeeDTO> findAll() {
        return employeeService.findAll();
    }

    @PostMapping()
    public ResponseEntity<EmployeeDTO> addEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO,
            BindingResult theBindingResult) {

        EmployeeDTO manager =
                employeeService.findManagerByDepartmentName(employeeDTO.getDepartmentName());

        EmployeeValidation.validateAll(employeeDTO, theBindingResult, manager);

        employeeService.save(employeeDTO);

        return new ResponseEntity<>(employeeDTO, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDTO updateEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO,
            BindingResult theBindingResult) {

        EmployeeValidation.validateAll(
                employeeDTO,
                theBindingResult,
                employeeService.findManagerByDepartmentName(employeeDTO.getDepartmentName()));

        employeeService.update(employeeDTO);

        return employeeDTO;
    }

    @PutMapping("/{employeeId}/dismiss")
    public EmployeeDTO dismissEmployee(
            @PathVariable long employeeId,
            @Valid @RequestParam("date") String dismissalDate) {

        return employeeService.dismissEmployee(employeeId, dismissalDate);
    }

    @GetMapping("/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {

        return employeeService.findById(employeeId);
    }

    @GetMapping("/department/{departmentId}")
    public List<EmployeeDTO> findEmployeesByDepartmentId(@PathVariable long departmentId) {

        return employeeService.findActualEmployeesByDepartmentId(departmentId);
    }

    @GetMapping("/department/{departmentId}/count")
    public Long countEmployeesByDepartmentId(@PathVariable long departmentId) {

        return employeeService.countActualEmployeesByDepartmentId(departmentId);
    }

    @GetMapping("/department/{departmentId}/manager")
    public EmployeeDTO findManagerByDepartmentId(@PathVariable long departmentId) {

        return employeeService.findManagerByDepartmentId(departmentId);
    }

    @GetMapping("/department/name/{departmentName}/manager")
    public EmployeeDTO findManagerByDepartmentName(@PathVariable String departmentName) {

        return employeeService.findManagerByDepartmentName(departmentName);
    }

    @PutMapping("/{employeeId}/transfer/{departmentId}")
    public EmployeeDTO transferEmployee(
            @PathVariable long employeeId,
            @PathVariable long departmentId) {

        return employeeService.transferEmployee(employeeId, departmentId);
    }

    @GetMapping("/{employeeId}/manager")
    public EmployeeDTO findManager(@PathVariable long employeeId) {

        return employeeService.findManagerOfEmployeeByEmployeeId(employeeId);
    }

    @GetMapping("/find")
    public List<EmployeeDTO> findFilteredEmployees(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String jobTitle) {

        return employeeService.findFilteredEmployees(lastName, firstName, jobTitle);
    }
}
