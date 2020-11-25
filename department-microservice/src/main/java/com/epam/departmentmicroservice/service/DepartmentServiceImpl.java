package com.epam.departmentmicroservice.service;

import com.epam.departmentmicroservice.dto.DepartmentDTO;
import com.epam.departmentmicroservice.dto.EmployeeDTO;
import com.epam.departmentmicroservice.entity.Department;
import com.epam.departmentmicroservice.exception.CannotExecuteException;
import com.epam.departmentmicroservice.exception.DepartmentNotFoundException;
import com.epam.departmentmicroservice.exception.EmptyResultException;
import com.epam.departmentmicroservice.exception.InvalidParametersException;
import com.epam.departmentmicroservice.feignclient.EmployeeClient;
import com.epam.departmentmicroservice.repository.DepartmentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private EmployeeClient employeeClient;

    static final Logger logger = LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    public DepartmentServiceImpl(
            DepartmentRepository theDepartmentRepository,
            EmployeeClient theEmployeeClient) {

        departmentRepository = theDepartmentRepository;
        employeeClient = theEmployeeClient;
    }

    @Override
    @Transactional
    public List<DepartmentDTO> findAll() {

        logger.info("Fetching all departments from database");

        List<Department> departments = departmentRepository.findAll();

        logger.info("Successfully found " + departments.size() + " departments");

        return departments.stream()
                .map(this::createDepartmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentDTO findById(Long theDepartmentId) {

        logger.info("Fetching department " + theDepartmentId + " from database");

        Optional<Department> possibleDepartment = departmentRepository.findById(theDepartmentId);

        Department department = possibleDepartment.orElseThrow(
                () -> new DepartmentNotFoundException("The department with id = " + theDepartmentId + " is not found"));

        return createDepartmentDTO(department);
    }

    @Override
    @Transactional
    public DepartmentDTO renameDepartment(Long theDepartmentId, String theDepartmentName) {

        Department department = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with id = " + theDepartmentId + " is not found"));

        try {

            department.setName(theDepartmentName);
            departmentRepository.save(department);
        } catch (DataIntegrityViolationException exc) {
            throw new CannotExecuteException("The name is already used by another department");
        }

        return createDepartmentDTO(department);

    }

    @Override
    @Transactional
    public String findNameById(Long theDepartmentId) {

        logger.info("Fetching name of department #" + theDepartmentId + " from database");

        Optional<Department> possibleDepartment = departmentRepository.findById(theDepartmentId);

        Department department = possibleDepartment.orElseThrow(
                () -> new DepartmentNotFoundException("The department with id = " + theDepartmentId + " is not found"));

        return department.getName();
    }

    @Override
    @Transactional
    public DepartmentDTO save(DepartmentDTO theDepartmentDTO) {

        logger.info("Saving new department to database");

        Department toSaveDepartment = new Department();
        convertFromDepartmentDTO(toSaveDepartment, theDepartmentDTO);

        toSaveDepartment = departmentRepository.save(toSaveDepartment);

        return createDepartmentDTO(toSaveDepartment);
    }

    /**
     * Deletes the Department with the id specified by the first argument from the database.
     *
     * <p>If a Department has actual employees then it can not be deleted.
     *
     * <p>If a Department has sub-departments, they become sub-departments of the Department
     * that is the parent of the Department being deleted
     *
     * @param theDepartmentId an id of a Department to be deleted
     */
    @Override
    @Transactional
    public void deleteById(Long theDepartmentId) {

        logger.info("Deleting a department " + theDepartmentId + " from database");

        Department department = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "Deleting failed! The department with the id = "
                                + theDepartmentId + " is not found: deleting failed")
        );

        long actualAmountOfEmployees =
                employeeClient.countEmployeesByDepartmentId(theDepartmentId);

        if (actualAmountOfEmployees != 0) {
            throw new CannotExecuteException(
                    "The department is not empty: " + actualAmountOfEmployees + " employees work there");
        }

        department.getSubDepartments()
                .forEach((n) -> n.setParentDepartment(department.getParentDepartment()));

        try {
            departmentRepository.deleteById(theDepartmentId);
        } catch (DataIntegrityViolationException exc) {
            throw new CannotExecuteException(
                    "Can not be deleted: some constraints are still up");
        }
    }

    @Override
    @Transactional
    public DepartmentDTO findDepartmentByName(String theDepartmentName) {

        logger.info("Searching for department '" + theDepartmentName + "' in database");

        Department theDepartment = departmentRepository.findDepartmentByNameEquals(theDepartmentName);

        if (theDepartment == null) {
            throw new DepartmentNotFoundException(
                    "The department with the name = " + theDepartmentName + " is not found");
        }

        return createDepartmentDTO(theDepartment);
    }

    @Override
    @Transactional
    public Set<DepartmentDTO> findSubDepartmentsByParentId(Long theDepartmentId) {

        logger.info("Searching for sub-departments of department " + theDepartmentId + " in database");

        Department department = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + theDepartmentId + " is not found")
        );

        if (department.getSubDepartments() == null || department.getSubDepartments().size() == 0) {
            throw new EmptyResultException("The department does not have any sub departments");
        }

        return department.getSubDepartments().stream()
                .map(this::createDepartmentDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<DepartmentDTO> findAllSubDepartmentsByParentId(Long theDepartmentId) {

        logger.info("Searching for all sub-departments of department " + theDepartmentId + " in database");

        Department department = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + theDepartmentId + " is not found")
        );

        if (department.getSubDepartments() == null || department.getSubDepartments().size() == 0) {
            throw new EmptyResultException("The department does not have any sub departments");
        }

        Set<DepartmentDTO> allSubDepartments = new HashSet<>();
        getDepartmentsRecursively(department, allSubDepartments);

        return allSubDepartments;
    }

    /**
     * Change parent of a department
     *
     * @param toMoveDepartmentId department to change his parent
     * @param parentDepartmentId the parent
     * @return success message
     */
    @Override
    @Transactional
    public String moveDepartment(Long toMoveDepartmentId, Long parentDepartmentId) {

        logger.info("Moving department #" + toMoveDepartmentId + " to #" + parentDepartmentId);

        if (toMoveDepartmentId.equals(parentDepartmentId)) {
            throw new InvalidParametersException(
                    "Specified parameters are the same. Department can not be parent of itself");
        }

        Department department = departmentRepository.findById(toMoveDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + toMoveDepartmentId + " is not found")
        );

        Department parentDepartment = departmentRepository.findById(parentDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + toMoveDepartmentId + " is not found")
        );

        department.setParentDepartment(parentDepartment);

        departmentRepository.save(department);

        return "The department #" + toMoveDepartmentId + " is moved successfully" +
                "to parent department #" + parentDepartment.getDepartmentId();
    }

    @Override
    @Transactional
    public Set<DepartmentDTO> findAllParentDepartmentsById(Long theDepartmentId) {

        logger.info("Searching for all parent departments of department " + theDepartmentId + " in database");

        Department theDepartment = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + theDepartmentId + " is not found")
        );

        if (theDepartment.getParentDepartment() == null) {
            throw new DepartmentNotFoundException("The department does not have any parent department");
        }

        Set<DepartmentDTO> allParentDepartments = new HashSet<>();

        Department tempDepartment = theDepartment.getParentDepartment();

        while (tempDepartment != null) {
            allParentDepartments.add(createDepartmentDTO(tempDepartment));
            tempDepartment = tempDepartment.getParentDepartment();
        }

        return allParentDepartments;
    }

    /**
     * Calculate the fund of a department
     *
     * @param theDepartmentId the department to work with
     * @return the fund (russian rubles)
     */
    @Override
    @Transactional
    public Integer sumEmployeeSalaryById(Long theDepartmentId) {

        logger.info("Calculating the fund of department " + theDepartmentId + " in database");

        Department department = departmentRepository.findById(theDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + theDepartmentId + " is not found")
        );

        return employeeClient.findEmployeesByDepartmentId(theDepartmentId).stream()
                .filter((n) -> n.getDismissalDate() == null)
                .mapToInt(EmployeeDTO::getSalary).sum();
    }

    @Override
    @Transactional
    public Long findDepartmentIdByDepartmentName(String theDepartmentName) {

        logger.info("Searching for the departmentId of department '" + theDepartmentName + "'");

        Department theDepartment = departmentRepository.findDepartmentByNameEquals(theDepartmentName);

        if (theDepartment == null) {
            throw new DepartmentNotFoundException(
                    "The department with the name = " + theDepartmentName + " is not found");
        }

        return theDepartment.getDepartmentId();
    }

    @Override
    @Transactional
    public DepartmentDTO transferEmployeesFromIdToNewId(Long fromDepartmentId, Long toDepartmentId) {

        logger.info("Transferring employees from department " + fromDepartmentId +
                " to department " + toDepartmentId);

        if (!departmentRepository.existsById(fromDepartmentId)) {
            throw new DepartmentNotFoundException(
                    "The department with the id = " + fromDepartmentId + " is not found");
        }
        Department toDepartment = departmentRepository.findById(toDepartmentId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        "The department with the id = " + toDepartmentId + " is not found")
        );

        for (EmployeeDTO anEmployee : employeeClient.findEmployeesByDepartmentId(fromDepartmentId)) {
            employeeClient.transferEmployee(anEmployee.getEmployeeId(), toDepartmentId);
        }

        return createDepartmentDTO(toDepartment);
    }

    private void getDepartmentsRecursively(Department theDepartment, Set<DepartmentDTO> toCollect) {

        Set<Department> currentSubDepartments = theDepartment.getSubDepartments();

        if (currentSubDepartments == null) {
            return;
        }

        toCollect.addAll(currentSubDepartments.stream()
                .map(this::createDepartmentDTO)
                .collect(Collectors.toSet()));

        for (Department aDepartment : theDepartment.getSubDepartments()) {
            getDepartmentsRecursively(aDepartment, toCollect);
        }
    }

    private void convertFromDepartmentDTO(Department department, DepartmentDTO departmentDTO) {

        department.setName(departmentDTO.getName());
        department.setCreationDate(departmentDTO.getCreationDate());

        if (departmentDTO.getDepartmentId() != 0L) {
            department.setDepartmentId(departmentDTO.getDepartmentId());
        }

        if (departmentDTO.getParentDepartmentId() != null && departmentDTO.getParentDepartmentId() != 0L) {

            department.setParentDepartment(departmentRepository.findById(departmentDTO.getParentDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            "The specified parent department for '" + department.getName() + "' is not found")));
        } else {
            department.setParentDepartment(null);
        }
    }

    private DepartmentDTO createDepartmentDTO(Department department) {
        return new DepartmentDTO(
                department,
                employeeClient.findManagerByDepartmentId(department.getDepartmentId()),
                employeeClient.countEmployeesByDepartmentId(department.getDepartmentId()));
    }
}
