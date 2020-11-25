package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.dto.DepartmentDTO;
import com.epam.employeemicroservice.entity.DepartmentSnapshot;
import com.epam.employeemicroservice.feignclient.DepartmentClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Provides the logic for the department snapshot
 * in order to reduce the number of feign requests
 */
@Service
public class DepartmentResolverService implements DepartmentClient {

    private DepartmentClient departmentClient;
    private DepartmentSnapshotService departmentSnapshotService;
    private Set<Long> changedDepartments;

    static final Logger logger = LogManager.getLogger(DepartmentResolverService.class);


    @Autowired
    public DepartmentResolverService(
            DepartmentClient theDepartmentClient,
            DepartmentSnapshotService theDepartmentSnapshotService,
            Set<Long> theChangeDepartments) {

        this.departmentClient = theDepartmentClient;
        this.departmentSnapshotService = theDepartmentSnapshotService;
        this.changedDepartments = theChangeDepartments;
    }

    @Override
    public List<DepartmentDTO> findAll() {
        return departmentClient.findAll();
    }

    @Override
    public DepartmentDTO findById(Long departmentId) {

        logger.info("Fetching department from department microservice");

        return departmentClient.findById(departmentId);
    }

    @Override
    public String findDepartmentNameByDepartmentId(Long departmentId) {

        boolean isChangesDetected = changedDepartments.contains(departmentId);

        if (!isChangesDetected) {

            Optional<String> possibleDepartmentName = departmentSnapshotService.getNameById(departmentId);
            if (possibleDepartmentName.isPresent()) {
                return possibleDepartmentName.get();
            }

            logger.info("Did not found the department in the snapshot");

        } else {
            logger.info("Changes for the department were detected");
        }

        logger.info("Fetching department name from department microservice");

        String searchedDepartmentName = departmentClient.findDepartmentNameByDepartmentId(departmentId);
        departmentSnapshotService.save(new DepartmentSnapshot(departmentId, searchedDepartmentName));

        if (isChangesDetected) {
            changedDepartments.remove(departmentId);
        }

        return searchedDepartmentName;
    }

    @Override
    public Long findDepartmentIdByDepartmentName(String departmentName) {

        Optional<Long> possibleDepartmentId = departmentSnapshotService.getIdByName(departmentName);
        boolean isChangesDetected = false;

        if (possibleDepartmentId.isPresent()) {
            isChangesDetected = changedDepartments.contains(possibleDepartmentId.get());
            if (!isChangesDetected) {
                return possibleDepartmentId.get();
            } else {
                logger.info("Changes for the department were detected");
            }
        } else {
            logger.info("Did not found the department in the snapshot");
        }

        logger.info("Fetching department id from department microservice");

        Long departmentId = departmentClient.findDepartmentIdByDepartmentName(departmentName);
        if (departmentId == null) {
            departmentSnapshotService.deleteByName(departmentName);
            return departmentId;
        }
        departmentSnapshotService.save(new DepartmentSnapshot(departmentId, departmentName));

        if (isChangesDetected) {
            changedDepartments.remove(departmentId);
        }

        return departmentId;
    }

    public void clearDepartmentSnapshot() {

        departmentSnapshotService.deleteAll();
    }
}
