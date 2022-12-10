package com.dev6.rejordbe.exception;

/**
 * DuplicatedUserIdException
 */
public class DuplicatedUserIdException extends RuntimeException {
    public DuplicatedUserIdException(final String message) {
        super(message);
    }
}
