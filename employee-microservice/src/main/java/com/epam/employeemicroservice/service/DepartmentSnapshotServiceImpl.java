package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.entity.DepartmentSnapshot;
import com.epam.employeemicroservice.repository.DepartmentSnapshotRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentSnapshotServiceImpl implements DepartmentSnapshotService {

    private DepartmentSnapshotRepository departmentSnapshotRepository;


    static final Logger logger = LogManager.getLogger(DepartmentSnapshotServiceImpl.class);

    @Autowired
    public DepartmentSnapshotServiceImpl(DepartmentSnapshotRepository departmentSnapshotRepository) {

        this.departmentSnapshotRepository = departmentSnapshotRepository;
    }

    @Override
    public Optional<String> getNameById(Long id) {

        logger.info("Fetching department name from the snapshot");

        return Optional.ofNullable(departmentSnapshotRepository.getNameById(id));
    }

    @Override
    public Optional<Long> getIdByName(String name) {

        logger.info("Fetching department id from the snapshot");

        return Optional.ofNullable(departmentSnapshotRepository.getIdByName(name));
    }

    @Override
    public DepartmentSnapshot save(DepartmentSnapshot theDomain) {

        logger.info("Refreshing department the snapshot");

        return departmentSnapshotRepository.save(theDomain);
    }

    @Override
    public void deleteById(Long id) {

        logger.info("Deleting department from the snapshot");

        departmentSnapshotRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {

        logger.info("Deleting department from the snapshot");
        
        departmentSnapshotRepository.deleteByName(name);
    }
}
