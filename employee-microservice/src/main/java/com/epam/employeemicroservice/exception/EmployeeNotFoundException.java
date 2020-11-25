package com.epam.employeemicroservice.exception;

/**
 * The exception that is thrown when the employee specified by
 * its id is not found in the database
 */
public class EmployeeNotFoundException extends ResourceNotFoundException {

    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
