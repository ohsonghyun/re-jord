package com.dev6.rejordbe.presentation.controller.user.login;

import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * UserControllerAdvice
 */
@RestControllerAdvice(basePackageClasses = LoginController.class)
@Slf4j
public class LoginControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(UserNotFoundException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder().message(exception.getMessage()).build(),
                headers(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(BadCredentialsException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder().message(exception.getMessage()).build(),
                headers(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(
                ErrorResponse.builder().message("INTERNAL_SERVER_ERROR").build(),
                headers(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ----------------------------------------------

    private HttpHeaders headers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return headers;
    }
}
