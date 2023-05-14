package com.dev6.rejordbe.presentation.controller.post.delete;

import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.PostNotFoundException;
import com.dev6.rejordbe.exception.UnauthorizedUserException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * DeletePostControllerAdvice
 */
@RestControllerAdvice(basePackageClasses = DeletePostController.class)
@Slf4j
public class DeletePostControllerAdvice {

    @ExceptionHandler(IllegalParameterException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(IllegalParameterException exception){
        return new ResponseEntity<>(
                ErrorResponse.builder().message(exception.getMessage()).build(),
                headers(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(PostNotFoundException exception){
        return new ResponseEntity<>(
                ErrorResponse.builder().message(exception.getMessage()).build(),
                headers(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(UnauthorizedUserException exception){
        return new ResponseEntity<>(
                ErrorResponse.builder().message(exception.getMessage()).build(),
                headers(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(
                ErrorResponse.builder().message("INTERNAL_SERVER_ERROR").build(),
                headers(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders headers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return headers;
    }
}
