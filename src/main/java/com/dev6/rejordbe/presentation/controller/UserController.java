package com.dev6.rejordbe.presentation.controller;

import com.dev6.rejordbe.application.user.signup.SignUpService;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 */
@RestController
@RequestMapping("/v1/users")
@lombok.RequiredArgsConstructor
public class UserController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody final SignUpRequest signUpUserRequest) {
        UserResult savedResult = signUpService.signUp(signUpUserRequest.toUser());

        // 에러. 회원가입 실패
        if (savedResult.hasErrors()) {
            return ResponseEntity
                    .status(
                            savedResult.getErrors().stream()
                                    .filter(ex -> ex instanceof IllegalParameterException)
                                    .findFirst()
                                    .isEmpty()
                            ? HttpStatus.CONFLICT
                            : HttpStatus.BAD_REQUEST)
                    .body(
                            SignUpResponse.builder()
                                    .errors(
                                            savedResult.getErrors().stream()
                                                    .map(RuntimeException::getMessage)
                                                    .toList())
                                    .build());
        }

        // 회원가입 성공
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SignUpResponse.builder()
                        .uid(savedResult.getUid())
                        .userId(savedResult.getUserId())
                        .nickname(savedResult.getNickname())
                        .userType(savedResult.getUserType())
                        .build());
    }
}
