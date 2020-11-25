package com.epam.departmentmicroservice.exception;

/**
 * The exception that is thrown when the request parameters are not valid
 */
public class InvalidParametersException extends RuntimeException {

    public InvalidParametersException() {
    }

    public InvalidParametersException(String message) {
        super(message);
    }

    public InvalidParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
