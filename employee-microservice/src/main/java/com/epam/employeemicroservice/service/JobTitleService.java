package com.epam.employeemicroservice.service;

import com.epam.employeemicroservice.entity.JobTitle;

public interface JobTitleService extends BaseService<JobTitle> {

    JobTitle findJobTitleByName(String title);
}
