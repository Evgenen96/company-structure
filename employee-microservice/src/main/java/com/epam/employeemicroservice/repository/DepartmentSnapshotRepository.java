package com.epam.employeemicroservice.repository;

import com.epam.employeemicroservice.entity.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentSnapshotRepository extends JpaRepository<DepartmentSnapshot, Long> {

    @Query("SELECT d.name FROM DepartmentSnapshot d WHERE d.id = ?1")
    String getNameById(Long id);

    @Query("SELECT d.id FROM DepartmentSnapshot d WHERE d.name = ?1")
    Long getIdByName(String name);

    @Query("DELETE FROM DepartmentSnapshot d WHERE d.name = ?1")
    void deleteByName(String name);
}
