package com.epam.departmentmicroservice.exception;

/**
 * The exception that is thrown when the department specified by
 * its id or name is not found in the database
 */
public class DepartmentNotFoundException extends ResourceNotFoundException {

    public DepartmentNotFoundException() {
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
