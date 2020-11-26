package com.epam.employeemicroservice.feignclient;

import com.epam.employeemicroservice.dto.DepartmentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DepartmentClientFallback implements DepartmentClient {

    private Throwable cause;

    static final Logger logger = LogManager.getLogger(DepartmentClientFallback.class);

    public DepartmentClientFallback(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public List<DepartmentDTO> findAll() {

        logger.error(cause);

        return new LinkedList<>();
    }

    @Override
    public DepartmentDTO findById(Long departmentId) {

        logger.error(cause);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Department is not found");

        return departmentDTO;
    }

    @Override
    public String findDepartmentNameByDepartmentId(Long departmentId) {

        logger.error(cause);

        return "Department is not found";
    }

    @Override
    public Long findDepartmentIdByDepartmentName(String departmentName) {

        logger.error(cause);

        return 0L;
    }
}
