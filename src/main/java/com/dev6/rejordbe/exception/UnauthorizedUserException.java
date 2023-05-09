package com.dev6.rejordbe.exception;

/**
 * UnauthorizedUserException
 */
public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
