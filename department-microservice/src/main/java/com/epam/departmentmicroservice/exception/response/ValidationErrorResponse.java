package com.epam.departmentmicroservice.exception.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * {@code ValidationErrorResponse} class represents data transfer object for
 */
public class ValidationErrorResponse {

    private int httpStatus;

    private String error;

    private String timeStamp;

    /**
     * Collecting all the validation errors in this list
     */
    private List<String> validationErrors;

    @JsonIgnore
    private final SimpleDateFormat gmtDateFormat;

    public ValidationErrorResponse() {
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        validationErrors = new LinkedList<>();
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = gmtDateFormat.format(timeStamp);
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
