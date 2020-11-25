package com.epam.employeemicroservice.repository;

import com.epam.employeemicroservice.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {
    JobTitle findJobTitleByTitle(String title);
}
