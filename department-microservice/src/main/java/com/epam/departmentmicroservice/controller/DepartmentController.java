package com.epam.departmentmicroservice.controller;

import com.epam.departmentmicroservice.dto.DepartmentDTO;
import com.epam.departmentmicroservice.exception.InvalidParametersException;
import com.epam.departmentmicroservice.kafka.KafkaTopics;
import com.epam.departmentmicroservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private DepartmentService departmentService;
    private KafkaTemplate<String, Long> kafkaTemplate;

    private final String DEPARTMENT_TOPIC = KafkaTopics.DEPARTMENTS.name();

    @Autowired
    public DepartmentController(
            DepartmentService theDepartmentService,
            KafkaTemplate<String, Long> theKafkaTemplate) {

        departmentService = theDepartmentService;
        kafkaTemplate = theKafkaTemplate;
    }

    @GetMapping
    public List<DepartmentDTO> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/{departmentId}")
    public DepartmentDTO findDepartment(@PathVariable long departmentId) {

        return departmentService.findById(departmentId);
    }

    @PostMapping()
    public ResponseEntity<DepartmentDTO> addDepartment(
            @RequestBody DepartmentDTO departmentDTO,
            @RequestParam(required = false) Long parentDepartmentId) {

        departmentDTO.setDepartmentId(0L);
        departmentDTO.setParentDepartmentId(parentDepartmentId);

        departmentDTO = departmentService.save(departmentDTO);

        kafkaTemplate.send(DEPARTMENT_TOPIC, departmentDTO.getDepartmentId());

        return new ResponseEntity<>(departmentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{departmentId}/rename/{departmentName}")
    public DepartmentDTO renameDepartment(
            @PathVariable long departmentId,
            @PathVariable String departmentName) {

        DepartmentDTO renamedDepartmentDTO = departmentService.renameDepartment(departmentId, departmentName);

        kafkaTemplate.send(DEPARTMENT_TOPIC, departmentId);

        return renamedDepartmentDTO;
    }

    @DeleteMapping("/{departmentId}")
    public String removeDepartment(@PathVariable long departmentId) {

        departmentService.deleteById(departmentId);

        kafkaTemplate.send(DEPARTMENT_TOPIC, departmentId);

        return "The department is successfully deleted";
    }

    @GetMapping("/{parentId}/sub")
    public Set<DepartmentDTO> findSubDepartments(@PathVariable long parentId) {

        return departmentService.findSubDepartmentsByParentId(parentId);
    }

    @GetMapping("/{departmentId}/sub/all")
    public Set<DepartmentDTO> findAllSubDepartments(@PathVariable long departmentId) {

        return departmentService.findAllSubDepartmentsByParentId(departmentId);
    }

    @PutMapping("/{departmentId}/move/{parentDepartmentId}")
    public String moveDepartment(
            @PathVariable long departmentId,
            @PathVariable long parentDepartmentId) {

        return departmentService.moveDepartment(departmentId, parentDepartmentId);
    }

    @GetMapping("/{departmentId}/parents")
    public Set<DepartmentDTO> findParentDepartments(@PathVariable long departmentId) {

        return departmentService.findAllParentDepartmentsById(departmentId);
    }

    @GetMapping("/name/{departmentName}/id")
    public Long findDepartmentIdByDepartmentName(@PathVariable String departmentName) {

        return departmentService.findDepartmentIdByDepartmentName(departmentName);

    }

    @GetMapping("/{departmentId}/name")
    String findDepartmentNameByDepartmentId(@PathVariable long departmentId) {

        return departmentService.findNameById(departmentId);
    }

    @GetMapping("/{departmentId}/fund")
    public Integer findDepartmentFund(@PathVariable long departmentId) {

        return departmentService.sumEmployeeSalaryById(departmentId);
    }

    @PutMapping("/{fromDepartmentId}/employees/transfer/{toDepartmentId}")
    public String transferEmployees(
            @PathVariable long fromDepartmentId,
            @PathVariable long toDepartmentId) {

        if (fromDepartmentId == toDepartmentId) {
            throw new InvalidParametersException(
                    "Specified parameters are the same: no sense to transfer");
        }

        departmentService.transferEmployeesFromIdToNewId(fromDepartmentId, toDepartmentId);

        return "All the department employees are successfully transferred!";
    }

}