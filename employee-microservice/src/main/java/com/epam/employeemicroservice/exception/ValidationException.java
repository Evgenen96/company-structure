package com.epam.employeemicroservice.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationException extends RuntimeException {

    private List<ObjectError> errors;

    public ValidationException(List<ObjectError> errors) {
        super();
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
