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
 * Provides the work logic for the department snapshot
 * in order to reduce the number of feign requests.
 * <p> Simply put, checks if there any kafka messages (in Set changedDepartments) which mean
 * that particular department was renamed or deleted.
 * <p> If changes are not detected, then it tries to find data from the {@link DepartmentSnapshot}.
 * <p> If fails then it makes a request by feign client ({@link DepartmentClient}).
 */
@Service
public class DepartmentResolverService implements DepartmentClient {

    private DepartmentClient departmentClient;
    private DepartmentSnapshotService departmentSnapshotService;
    /** Storing kafka messages here */
    private Set<Long> changedDepartmentsFromKafka;

    static final Logger logger = LogManager.getLogger(DepartmentResolverService.class);


    @Autowired
    public DepartmentResolverService(
            DepartmentClient theDepartmentClient,
            DepartmentSnapshotService theDepartmentSnapshotService,
            Set<Long> theChangedDepartments) {

        this.departmentClient = theDepartmentClient;
        this.departmentSnapshotService = theDepartmentSnapshotService;
        this.changedDepartmentsFromKafka = theChangedDepartments;
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

        boolean isChangesDetected = changedDepartmentsFromKafka.contains(departmentId);

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
            changedDepartmentsFromKafka.remove(departmentId);
        }

        return searchedDepartmentName;
    }

    @Override
    public Long findDepartmentIdByDepartmentName(String departmentName) {

        Optional<Long> possibleDepartmentId = departmentSnapshotService.getIdByName(departmentName);
        boolean isChangesDetected = false;

        if (possibleDepartmentId.isPresent()) {
            isChangesDetected = changedDepartmentsFromKafka.contains(possibleDepartmentId.get());
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
        if (departmentId == 0L) {
            departmentSnapshotService.deleteByName(departmentName);
            return departmentId;
        }
        departmentSnapshotService.save(new DepartmentSnapshot(departmentId, departmentName));

        if (isChangesDetected) {
            changedDepartmentsFromKafka.remove(departmentId);
        }

        return departmentId;
    }

    public void clearDepartmentSnapshot() {

        departmentSnapshotService.deleteAll();
    }
}
