package com.dev6.rejordbe.presentation.controller;

import com.dev6.rejordbe.application.user.signup.SignUpService;
import com.dev6.rejordbe.application.user.userinfo.UserInfoService;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest;
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpResponse;
import com.dev6.rejordbe.presentation.controller.dto.userInfo.UpdateUserInfoRequest;
import com.dev6.rejordbe.presentation.controller.dto.userInfo.UpdateUserInfoResponse;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 */
@Api(tags = "유저 컨트롤러")
@RestController
@RequestMapping("/v1/users")
@lombok.RequiredArgsConstructor
public class UserController {

    private final SignUpService signUpService;
    private final UserInfoService userInfoService;

    @ApiOperation(
            value = "회원가입",
            nickname = "signUp",
            notes = "회원가입API. 에러인 경우에는 errors 필드에만 값 설정 후 리스폰스",
            response = SignUpResponse.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "Users")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상"),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = SignUpResponse.class),
            @ApiResponse(code = 409, message = "존재하는 유저ID 또는 닉네임", response = SignUpResponse.class)
    })
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SignUpResponse> signUp(@ApiParam(value = "회원가입 정보", required = true) @RequestBody final SignUpRequest signUpUserRequest) {
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

    @ApiOperation(
            value = "회원 정보 수정",
            nickname = "updateUserInfo",
            notes = "회원 정보 수정 API. 현재 닉네임만 수정 가능.",
            response = UpdateUserInfoResponse.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 정보 변경 성공"),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 유저", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "이미 존재하는 닉네임", response = ErrorResponse.class)
    })
    @PatchMapping(
            value = "/{uid}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UpdateUserInfoResponse> updateUserInfo(
            @Schema(description = "유저 uid", required = true) @NonNull @PathVariable String uid,
            @ApiParam(value = "수정할 회원 정보", required = true) @RequestBody UpdateUserInfoRequest request
    ) {
        UserResult updatedUser = userInfoService.updateUserInfo(
                Users.builder().uid(uid).nickname(request.getNickname()).build());
        return ResponseEntity.ok(UpdateUserInfoResponse.builder()
                .uid(updatedUser.getUid())
                .userId(updatedUser.getUserId())
                .nickname(updatedUser.getNickname())
                .userType(updatedUser.getUserType())
                .build());
    }
}
