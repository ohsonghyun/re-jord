package com.dev6.rejordbe.exception;

/**
 * PostNotFoundException
 */
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
