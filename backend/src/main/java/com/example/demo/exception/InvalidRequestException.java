package com.example.demo.exception;

/**
 * Exception thrown when a request contains invalid data or violates business rules.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
