package com.epam.employeemicroservice.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс, отображающий таблицу-справочник существующих должностей
 */
@Entity
@Table(name = "job_title")
public class JobTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_title_id")
    private Long jobTitleId;

    @Column(name = "title")
    private String title;

    public JobTitle() {
    }

    public JobTitle(String title) {
        this.title = title;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "JobTitle{" +
                "jobTitleId=" + jobTitleId +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobTitle)) return false;
        JobTitle jobTitle = (JobTitle) o;
        return Objects.equals(jobTitleId, jobTitle.jobTitleId) &&
                Objects.equals(title, jobTitle.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobTitleId, title);
    }
}
