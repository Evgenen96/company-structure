package com.epam.employeemicroservice.exception;

/**
 * The exception that is thrown in some cases when a query
 * statement returns an empty collection
 */
public class EmptyResultException extends ResourceNotFoundException {

    public EmptyResultException() {
    }

    public EmptyResultException(String message) {
        super(message);
    }

    public EmptyResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
