package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.entity.JobTitle;
import com.epam.employeemicroservice.exception.ResourceNotFoundException;
import com.epam.employeemicroservice.repository.JobTitleRepository;
import com.epam.employeemicroservice.service.JobTitleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobTitleServiceImplTest {

    @Mock
    private JobTitleRepository jobTitleRepository;
    @InjectMocks
    private JobTitleServiceImpl jobTitleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll() {

        JobTitle jobTitle1 = new JobTitle("Разработчик");
        jobTitle1.setJobTitleId(1L);
        JobTitle jobTitle2 = new JobTitle("Тестировщик");
        jobTitle2.setJobTitleId(2L);
        JobTitle jobTitle3 = new JobTitle("Дизайнер");
        jobTitle3.setJobTitleId(3L);
        JobTitle jobTitle4 = new JobTitle("Девопс");
        jobTitle4.setJobTitleId(4L);

        List<JobTitle> expectedResult = new LinkedList<>();
        expectedResult.add(jobTitle1);
        expectedResult.add(jobTitle2);
        expectedResult.add(jobTitle3);
        expectedResult.add(jobTitle4);


        when(jobTitleRepository.findAll())
                .thenReturn(expectedResult);

        List<JobTitle> actualResult = jobTitleService.findAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void findById() {

        Long testId = 1L;
        JobTitle jobTitle = new JobTitle("Разработчик");

        when(jobTitleRepository.findById(eq(testId)))
                .thenReturn(Optional.of(jobTitle));

        JobTitle foundJobTitle = jobTitleService.findById(testId);

        assertNotNull(foundJobTitle);
        assertEquals(jobTitle, foundJobTitle);
    }

    @Test
    void save() {

        JobTitle jobTitle = new JobTitle("Разработчик");

        when(jobTitleRepository.save(any()))
                .thenReturn(jobTitle);

        JobTitle savedJobTitleTo = jobTitleService.save(jobTitle);

        assertNotNull(savedJobTitleTo);
        assertEquals(jobTitle, savedJobTitleTo);
    }

    @Test
    void deleteById() {

    }

    @Test
    void findJobTitleByName() {

        String testName = "Разработчик";
        JobTitle jobTitle = new JobTitle("Разработчик");
        jobTitle.setJobTitleId(1L);

        when(jobTitleRepository.findJobTitleByTitle(any()))
                .thenReturn(jobTitle);

        JobTitle foundJobTitle = jobTitleService.findJobTitleByName(testName);

        assertNotNull(foundJobTitle);
        assertEquals(jobTitle, foundJobTitle);
    }

    @Test
    void throwResourceNotFoundException() {

        when(jobTitleRepository.findById(any()))
                .thenReturn(Optional.empty());
        when(jobTitleRepository.findJobTitleByTitle(any()))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> jobTitleService.findById(1L));

        assertThrows(ResourceNotFoundException.class,
                () -> jobTitleService.findJobTitleByName("testName"));

        assertThrows(ResourceNotFoundException.class,
                () -> jobTitleService.deleteById(1L));
    }
}