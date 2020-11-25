package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.entity.JobTitle;
import com.epam.employeemicroservice.exception.ResourceNotFoundException;
import com.epam.employeemicroservice.repository.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JobTitleServiceImpl implements JobTitleService {

    private JobTitleRepository jobTitleRepository;

    @Autowired
    public JobTitleServiceImpl(JobTitleRepository theJobTitleRepository) {
        jobTitleRepository = theJobTitleRepository;
    }

    @Override
    @Transactional
    public List<JobTitle> findAll() {
        return jobTitleRepository.findAll();
    }

    @Override
    @Transactional
    public JobTitle findById(Long theId) {

        Optional<JobTitle> result = jobTitleRepository.findById(theId);

        JobTitle theJobTitle = null;

        if (result.isPresent()) {
            theJobTitle = result.get();
        } else {
            throw new ResourceNotFoundException("The jobTitle with id = " + theId + " is not found");
        }

        return theJobTitle;
    }

    @Override
    @Transactional
    public JobTitle save(JobTitle theJobTitle) {
        return jobTitleRepository.save(theJobTitle);
    }

    @Override
    @Transactional
    public void deleteById(Long theId) {

        if (jobTitleRepository.findById(theId).isPresent()) {
            jobTitleRepository.deleteById(theId);
        } else {
            throw new ResourceNotFoundException(
                    "The jobTitle with the id = " + theId + " is not found: deleting failed");
        }
    }

    @Override
    public JobTitle findJobTitleByName(String theTitle) {

        JobTitle jobTitle = jobTitleRepository.findJobTitleByTitle(theTitle);
        if (jobTitle != null) {
            return jobTitle;
        } else {
            throw new ResourceNotFoundException(
                    "The jobTitle with the name = " + theTitle + " is not found");
        }
    }
}
