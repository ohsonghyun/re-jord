package com.dev6.rejordbe.exception;

/**
 * WrongPasswordException
 */
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
