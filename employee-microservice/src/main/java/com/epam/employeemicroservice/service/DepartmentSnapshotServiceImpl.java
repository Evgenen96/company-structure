package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.repository.DepartmentSnapshotRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentSnapshotServiceImpl implements DepartmentSnapshotService {

    private DepartmentSnapshotRepository departmentSnapshotRepository;

    static final Logger logger = LogManager.getLogger(DepartmentSnapshotServiceImpl.class);

    @Autowired
    public DepartmentSnapshotServiceImpl(DepartmentSnapshotRepository departmentSnapshotRepository) {
        this.departmentSnapshotRepository = departmentSnapshotRepository;
    }

    @Override
    @Transactional
    public String getNameById(Long id) {

        logger.info("Fetching department name from snapshot");

        return departmentSnapshotRepository.getNameById(id);
    }

    @Override
    @Transactional
    public Long getIdByName(String name) {

        logger.info("Fetching department id from snapshot");

        return departmentSnapshotRepository.getIdByName(name);
    }
}
