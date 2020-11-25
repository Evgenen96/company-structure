package com.epam.departmentmicroservice.feignclient;

import com.epam.departmentmicroservice.dto.EmployeeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EmployeeClientFallback implements EmployeeClient {

    private Throwable cause;

    static final Logger logger = LogManager.getLogger(EmployeeClientFallback.class);

    public EmployeeClientFallback(Throwable cause) {

        this.cause = cause;
    }


    @Override
    public List<EmployeeDTO> findEmployeesByDepartmentId(long departmentId) {

        logger.error(cause);

        return new ArrayList<>();
    }

    @Override
    public Long countEmployeesByDepartmentId(long departmentId) {

        logger.error(cause);

        return 0L;
    }

    @Override
    public EmployeeDTO findManagerByDepartmentId(long departmentId) {

        logger.error(cause);

        return buildFallbackManager();

    }

    @Override
    public EmployeeDTO transferEmployee(long employeeId, long departmentId) {

        logger.error(cause);

        return null;
    }

    private EmployeeDTO buildFallbackManager() {

        EmployeeDTO manager = new EmployeeDTO();
        manager.setLastName("Manager is not found");
        manager.setFirstName("");
        manager.setPatronymic("");
        manager.setEmployeeId(0L);

        return manager;
    }
}
