package com.epam.departmentmicroservice.service;

import com.epam.departmentmicroservice.dto.DepartmentDTO;

import java.util.Set;

/**
 * Service layer interface for <i>departments</i>
 */
public interface DepartmentService extends BaseService<DepartmentDTO> {

    DepartmentDTO renameDepartment(Long id, String name);

    String findNameById(Long theId);

    DepartmentDTO findDepartmentByName(String departmentName);

    /**
     * @return Set of departments that are directly submissive for
     * the department specified by the id
     */
    Set<DepartmentDTO> findSubDepartmentsByParentId(Long id);

    /**
     * @return Set of all submissive departments for
     * the department specified by the id
     */
    Set<DepartmentDTO> findAllSubDepartmentsByParentId(Long id);

    String moveDepartment(Long toMoveDepartmentId, Long parentDepartmentId);

    /**
     * @return Set of all parent departments for
     * the department specified by the id
     */
    Set<DepartmentDTO> findAllParentDepartmentsById(Long id);

    /**
     * @return Sum of all employees salary in the department specified by the id
     */
    Integer sumEmployeeSalaryById(Long id);

    Long findDepartmentIdByDepartmentName(String departmentName);

    /**
     * Transfer all employees from one department to another.
     * @param fromId id of source department
     * @param toId id of target department
     */
    DepartmentDTO transferEmployeesFromIdToNewId(Long fromId, Long toId);
}
