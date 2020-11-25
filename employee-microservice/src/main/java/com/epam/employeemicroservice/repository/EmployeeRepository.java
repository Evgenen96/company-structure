package com.epam.employeemicroservice.repository;

import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findEmployeesByLastNameAndFirstNameOrJobTitle(String lastName, String firstName, JobTitle jobTitle);

    @Query("SELECT e FROM Employee e WHERE e.departmentId = ?1 and e.dismissalDate IS NULL")
    List<Employee> findActualEmployeesByDepartmentId(Long departmentId);

    @Query("SELECT count(e) FROM Employee e WHERE e.departmentId = ?1 and e.dismissalDate IS NULL")
    Long countActualEmployeesByDepartmentId(Long departmentId);

    @Query("SELECT e FROM Employee e " +
            "WHERE e.departmentId = ?1 and e.dismissalDate IS NULL and e.isManager = true")
    Employee findManagerByDepartmentId(long departmentId);
}
