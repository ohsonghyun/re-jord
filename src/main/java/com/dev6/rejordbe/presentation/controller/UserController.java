package com.dev6.rejordbe.presentation.controller;

import com.dev6.rejordbe.application.user.signup.SignUpService;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.SignUpUserRequestDto;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SignUpController
 */
public class UserController {

    private final SignUpService signUpService;

    @Autowired
    public UserController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    public ResponseEntity<UserResult> signUp(@Validated @RequestBody final SignUpUserRequestDto signUpUserRequestDto) {
        UserResult savedUser = signUpService.signUp(signUpUserRequestDto.toUsers());
        return ResponseEntity.ok(savedUser);
    }
}
