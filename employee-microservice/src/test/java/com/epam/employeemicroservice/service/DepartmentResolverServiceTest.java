package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.feignclient.DepartmentClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentResolverServiceTest {

    @Mock
    private DepartmentClient departmentClient;
    @Mock
    private DepartmentSnapshotService departmentSnapshotService;
    @Mock
    private Set<Long> changedDepartments;
    @InjectMocks
    private DepartmentResolverService departmentResolverService;

    private final Long testMessage = 1L;
    private final String testName = "World Hello";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void findDepartmentNameByDepartmentId_snapshotUse() {

        when(changedDepartments.contains(eq(testMessage)))
                .thenReturn(false);
        when(departmentSnapshotService.getNameById(testMessage))
                .thenReturn(Optional.of(testName));

        String gottenName = departmentResolverService.findDepartmentNameByDepartmentId(testMessage);

        assertNotNull(gottenName);
        assertEquals(testName, gottenName);
    }

    @Test
    void findDepartmentNameByDepartmentId_feignUse() {

        when(changedDepartments.contains(eq(testMessage)))
                .thenReturn(false);
        when(departmentSnapshotService.getNameById(testMessage))
                .thenReturn(Optional.empty());
        when(departmentClient.findDepartmentNameByDepartmentId(testMessage))
                .thenReturn(testName);

        String gottenName = departmentResolverService.findDepartmentNameByDepartmentId(testMessage);

        assertNotNull(gottenName);
        assertEquals(testName, gottenName);
    }

    @Test
    void findDepartmentIdByDepartmentName_snapshotUse() {

        when(departmentSnapshotService.getIdByName(testName))
                .thenReturn(Optional.of(testMessage));
        when(changedDepartments.contains(eq(testMessage)))
                .thenReturn(false);

        Long gottenId = departmentResolverService.findDepartmentIdByDepartmentName(testName);

        assertNotNull(gottenId);
        assertEquals(testMessage, gottenId);
    }

    @Test
    void findDepartmentIdByDepartmentName_feignUse() {

        when(departmentSnapshotService.getIdByName(testName))
                .thenReturn(Optional.empty());
        when(departmentClient.findDepartmentIdByDepartmentName(testName))
                .thenReturn(testMessage);

        Long gottenId = departmentResolverService.findDepartmentIdByDepartmentName(testName);

        assertNotNull(gottenId);
        assertEquals(testMessage, gottenId);
    }
}