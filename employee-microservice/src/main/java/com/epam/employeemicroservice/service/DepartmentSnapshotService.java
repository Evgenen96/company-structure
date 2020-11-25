package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.entity.DepartmentSnapshot;

import java.util.Optional;

public interface DepartmentSnapshotService {

    Optional<String> getNameById(Long id);

    Optional<Long> getIdByName(String name);

    DepartmentSnapshot save(DepartmentSnapshot departmentSnapshot);

    void deleteById(Long id);

    void deleteByName(String name);


}
