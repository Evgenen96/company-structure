package com.epam.departmentmicroservice.controller;

import com.epam.departmentmicroservice.exception.CannotExecuteException;
import com.epam.departmentmicroservice.exception.InvalidParametersException;
import com.epam.departmentmicroservice.exception.ResourceNotFoundException;
import com.epam.departmentmicroservice.exception.response.BasicErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {

    static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exc) {

        logger.warn(exc);

        BasicErrorResponse errorResponse = new BasicErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.name());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleInvalidParametersException(
            InvalidParametersException exc) {

        logger.warn(exc);

        BasicErrorResponse errorResponse = new BasicErrorResponse();

        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleDateTimeParseException(
            DateTimeParseException exc) {

        logger.warn(exc);

        BasicErrorResponse errorResponse = new BasicErrorResponse();

        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleJSONDeserializationException(
            HttpMessageNotReadableException exc) {

        BasicErrorResponse errorResponse = new BasicErrorResponse();

        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());

        if (exc.getCause() instanceof InvalidFormatException) {

            InvalidFormatException nestedExc = (InvalidFormatException) exc.getCause();
            String fieldName = nestedExc.getPath().get(0).getFieldName();
            if (fieldName.toLowerCase().contains("date")) {
                errorResponse.setMessage("The field " + fieldName + " is not correct (Should be 'yyyy-MM-dd' format");
            }
            errorResponse.setMessage("The field " + fieldName + " is not correct");

            logger.warn(nestedExc);

        } else if (exc.getCause() instanceof JsonParseException) {

            JsonParseException nestedExc = (JsonParseException) exc.getCause();
            errorResponse.setMessage(nestedExc.getMessage());

            logger.warn(nestedExc);

        } else {

            logger.warn(exc);

            errorResponse.setMessage(exc.getMessage());
        }
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleDBConnectionException(CannotCreateTransactionException exc) {

        logger.error(exc.getCause());

        BasicErrorResponse errorResponse = new BasicErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        errorResponse.setError(HttpStatus.SERVICE_UNAVAILABLE.name());
        errorResponse.setMessage(exc.getCause().getMessage());
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleCannotExecuteException(CannotExecuteException exc) {

        logger.warn(exc);

        BasicErrorResponse errorResponse = new BasicErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError(HttpStatus.CONFLICT.name());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleException(Exception exc)  {

        logger.error(exc);

        BasicErrorResponse errorResponse = new BasicErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
        errorResponse.setMessage("Oops, something is completely wrong :(");
        errorResponse.setTimeStamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
