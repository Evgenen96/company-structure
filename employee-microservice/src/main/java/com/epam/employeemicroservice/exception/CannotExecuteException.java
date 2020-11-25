package com.epam.employeemicroservice.exception;

/**
 * The exception that is thrown when a query statement is not executed
 * due to constraints violation
 */
public class CannotExecuteException extends RuntimeException {

    public CannotExecuteException() {
    }

    public CannotExecuteException(String message) {
        super(message);
    }

    public CannotExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
