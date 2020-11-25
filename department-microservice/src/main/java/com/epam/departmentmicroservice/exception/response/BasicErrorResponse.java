package com.epam.departmentmicroservice.exception.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * {@code BasicErrorResponse} class represents data transfer object for
 * the most types of exceptions in this project.
 */
public class BasicErrorResponse {

    private int httpStatus;

    private String error;

    private String message;

    private String timeStamp;
    
    @JsonIgnore
    private final SimpleDateFormat gmtDateFormat;


    public BasicErrorResponse() {
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+4"));
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
