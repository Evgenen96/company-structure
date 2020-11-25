package com.epam.employeemicroservice.service;

public interface DepartmentSnapshotService {

    String getNameById(Long id);

    Long getIdByName(String name);

}
