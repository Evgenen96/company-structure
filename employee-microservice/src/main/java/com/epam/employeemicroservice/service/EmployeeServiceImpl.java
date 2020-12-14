package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.entity.JobTitle;
import com.epam.employeemicroservice.exception.EmployeeNotFoundException;
import com.epam.employeemicroservice.exception.EmptyResultException;
import com.epam.employeemicroservice.exception.ResourceNotFoundException;
import com.epam.employeemicroservice.repository.EmployeeRepository;
import com.epam.employeemicroservice.repository.JobTitleRepository;
import com.epam.employeemicroservice.validation.EmployeeValidation;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private JobTitleRepository jobTitleRepository;
    private DepartmentResolverService departmentResolverService;

    @Autowired
    MeterRegistry registry;
    Counter cacheHitCounter;
    Counter cacheMissCounter;


    static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository theEmployeeRepository,
            JobTitleRepository theJobTitleRepository,
            DepartmentResolverService theDepartmentResolverService) {

        this.employeeRepository = theEmployeeRepository;
        this.jobTitleRepository = theJobTitleRepository;
        this.departmentResolverService = theDepartmentResolverService;
    }

    @PostConstruct
    private void clearDepartmentSnapshot() {
        departmentResolverService.clearDepartmentSnapshot();
        cacheHitCounter = Counter.builder("department.name.cache.hit.counter")
                .description("indicates count of cache hit for department names")
                .tag("cache", "hits")
                .tag("version", "1.0")
                .register(registry);
        cacheMissCounter = Counter.builder("department.name.cache.miss.counter")
                .description("indicates count of cache miss for department names")
                .tag("cache", "misses")
                .tag("version", "1.0")
                .register(registry);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> findAll() {

        logger.info("Fetching all employees from database");

        List<Employee> employees = employeeRepository.findAll();

        logger.info("Successfully found " + employees.size() + " employees");

        Map<Long, String> departmentCache = new WeakHashMap<>();

        return employees.stream()
                .map(employee -> createEmployeeDTO(employee, departmentCache))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO findById(Long theId) {

        logger.info("Fetching employee #" + theId + " from database");

        Optional<Employee> result = employeeRepository.findById(theId);

        Employee employee = result.orElseThrow(
                () -> new EmployeeNotFoundException("The employee #" + theId + " is not found")
        );

        return createEmployeeDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO save(EmployeeDTO theEmployeeDTO) {

        logger.info("Saving new employee to database");

        theEmployeeDTO.setEmployeeId(0L);
        Employee toSaveEmployee = new Employee();
        updateFromEmployeeDTO(toSaveEmployee, theEmployeeDTO);

        toSaveEmployee = employeeRepository.save(toSaveEmployee);
        theEmployeeDTO.setEmployeeId(toSaveEmployee.getEmployeeId());

        return theEmployeeDTO;
    }

    @Override
    @Transactional
    public EmployeeDTO update(EmployeeDTO theEmployeeDTO) {

        logger.info("Updating employee in database");

        Optional<Employee> result = employeeRepository.findById(theEmployeeDTO.getEmployeeId());

        if (result.isPresent()) {

            Employee toUpdateEmployee = result.get();
            updateFromEmployeeDTO(toUpdateEmployee, theEmployeeDTO);

            toUpdateEmployee = employeeRepository.save(toUpdateEmployee);

            theEmployeeDTO = createEmployeeDTO(toUpdateEmployee, theEmployeeDTO.getDepartmentName());

            return theEmployeeDTO;

        } else {
            throw new EmployeeNotFoundException(
                    "The employee #" + theEmployeeDTO.getEmployeeId() + " is not found: updating failed");
        }
    }

    @Override
    @Transactional
    public EmployeeDTO dismissEmployee(Long theEmployeeId, String theDismissalDate) {

        logger.info("Dismissing employee #" + theEmployeeId);

        Employee theEmployee = employeeRepository.findById(theEmployeeId).orElseThrow(
                () -> new EmployeeNotFoundException("The employee #" + theEmployeeId + " is not found"));

        theEmployee.setDismissalDate(LocalDate.parse(theDismissalDate));

        EmployeeValidation.validateDismissalDate(theEmployee);

        return createEmployeeDTO(employeeRepository.save(theEmployee));

    }

    @Override
    @Transactional
    public void deleteById(Long theId) {

        logger.info("Deleting employee #" + theId + " from database");

        if (employeeRepository.findById(theId).isPresent()) {
            employeeRepository.deleteById(theId);
        } else {
            throw new EmployeeNotFoundException(
                    "The employee #" + theId + " is not found: deleting failed");
        }
    }

    @Override
    @Transactional
    public List<EmployeeDTO> findFilteredEmployees(String lastName, String firstName, String jobTitle) {

        logger.info("Searching employees by filters");

        JobTitle theJobTitle = jobTitleRepository.findJobTitleByTitle(jobTitle);
        List<Employee> employees =
                employeeRepository.findEmployeesByLastNameAndFirstNameOrJobTitle(lastName, firstName, theJobTitle);

        if (employees.size() == 0) {
            throw new EmptyResultException("Such employees are not found");
        }

        Map<Long, String> departmentCache = new WeakHashMap<>();

        return employees.stream()
                .map(employee -> createEmployeeDTO(employee, departmentCache))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeDTO> findActualEmployeesByDepartmentId(Long departmentId) {

        logger.info("Searching for employees by department #" + departmentId);

        Map<Long, String> departmentCache = new WeakHashMap<>();

        return employeeRepository.findActualEmployeesByDepartmentId(departmentId).stream()
                .map(employee -> createEmployeeDTO(employee, departmentCache))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long countActualEmployeesByDepartmentId(long departmentId) {

        logger.info("Counting employees by the department #" + departmentId);

        return employeeRepository.countActualEmployeesByDepartmentId(departmentId);
    }

    @Override
    @Transactional
    public EmployeeDTO findManagerByDepartmentId(long departmentId) {

        if (departmentId == 0L) {
            throw new ResourceNotFoundException(
                    "The department was not found"
            );
        }

        logger.info("Searching for manager by department #" + departmentId);

        Employee employee = employeeRepository.findManagerByDepartmentId(departmentId);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "The manager of department #" + departmentId + " was not found");
        }

        return createEmployeeDTO(employee);
    }

    private EmployeeDTO findManagerByDepartmentId(long departmentId, String departmentName) {

        if (departmentId == 0L) {
            throw new ResourceNotFoundException(
                    "The department '" + departmentName + "' was not found"
            );
        }

        logger.info("Searching for manager by department #" + departmentId);

        Employee employee = employeeRepository.findManagerByDepartmentId(departmentId);

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    "The manager of department #" + departmentId + " was not found");
        }

        return createEmployeeDTO(employee, departmentName);
    }

    @Override
    @Transactional
    public EmployeeDTO findManagerByDepartmentName(String departmentName) {

        logger.info("Searching for manager by department name = " + departmentName);

        return findManagerByDepartmentId(
                departmentResolverService.findDepartmentIdByDepartmentName(departmentName), departmentName);
    }

    @Override
    @Transactional
    public EmployeeDTO findManagerOfEmployeeByEmployeeId(long employeeId) {

        logger.info("Searching for manager of employee by employee #" + employeeId);

        return findManagerByDepartmentName(findById(employeeId).getDepartmentName());
    }

    @Override
    @Transactional
    public EmployeeDTO transferEmployee(long theEmployeeId, long departmentId) {

        Employee theEmployee = employeeRepository.findById(theEmployeeId).orElseThrow(
                () -> new EmployeeNotFoundException(
                        "The employee #" + theEmployeeId + " is not found"));
        String theDepartmentName = departmentResolverService.findDepartmentNameByDepartmentId(departmentId);

        if (!theDepartmentName.equals("Department is not found")) {

            theEmployee.setDepartmentId(departmentId);

            EmployeeValidation.validateAll(
                    theEmployee,
                    null,
                    findManagerByDepartmentId(departmentId));

            employeeRepository.save(theEmployee);

            return createEmployeeDTO(theEmployee, theDepartmentName);

        } else {
            throw new ResourceNotFoundException(
                    "Transferring failed. Department with #" + departmentId + " is not found");
        }
    }

    private void updateFromEmployeeDTO(Employee employee, EmployeeDTO employeeDTO) {

        employee.setLastName(employeeDTO.getLastName());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setPatronymic(employeeDTO.getPatronymic());
        employee.setGender(Employee.Gender.getGender(employeeDTO.getGender()));
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setHiringDate(employeeDTO.getHiringDate());

        if (employeeDTO.getDismissalDate() != null) {
            employee.setDismissalDate(employeeDTO.getHiringDate());
        } else {
            employee.setDismissalDate(null);
        }

        JobTitle jobTitle = jobTitleRepository.findJobTitleByTitle(employeeDTO.getJobTitle());
        employee.setJobTitle(jobTitle);

        employee.setSalary(employeeDTO.getSalary());
        employee.setManager(employeeDTO.getManager());

        Long departmentId =
                departmentResolverService.findDepartmentIdByDepartmentName(employeeDTO.getDepartmentName());

        if (departmentId != 0L) {
            employee.setDepartmentId(departmentId);
        } else {
            throw new ResourceNotFoundException(
                    "Can not save/update employee. Department with the name: " +
                            employeeDTO.getDepartmentName() + " is not found");
        }
    }

    private EmployeeDTO createEmployeeDTO(Employee employee) {

        if (employee == null) {
            throw new EmployeeNotFoundException("The employee was not found");
        }
        return new EmployeeDTO(
                employee,
                departmentResolverService.findDepartmentNameByDepartmentId(employee.getDepartmentId()));
    }

    private EmployeeDTO createEmployeeDTO(Employee employee, Map<Long, String> departmentCache) {

        if (employee == null) {
            throw new EmployeeNotFoundException("The employee was not found");
        }

        String departmentName = departmentCache.get(employee.getDepartmentId());

        if (departmentName == null) {

            departmentName = departmentResolverService.findDepartmentNameByDepartmentId(employee.getDepartmentId());
            departmentCache.put(employee.getDepartmentId(), departmentName);
            cacheMissCounter.increment();
            return new EmployeeDTO(employee, departmentName);
        } else {
            cacheHitCounter.increment();
            return new EmployeeDTO(employee, departmentName);
        }
    }

    private EmployeeDTO createEmployeeDTO(Employee employee, String departmentName) {

        if (employee == null) {
            throw new EmployeeNotFoundException("The employee was not found");
        }
        return new EmployeeDTO(employee, departmentName);
    }
}
