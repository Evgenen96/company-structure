package com.epam.employeemicroservice.exception;

/**
 * The base exception that is thrown when the resource is not found in the database.

 * @see ResourceNotFoundException
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
