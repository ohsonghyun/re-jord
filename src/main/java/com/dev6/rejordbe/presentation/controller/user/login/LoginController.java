package com.dev6.rejordbe.presentation.controller.user.login;

import com.dev6.rejordbe.application.user.login.LoginService;
import com.dev6.rejordbe.domain.cookie.CookieNames;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.dev6.rejordbe.presentation.controller.dto.login.LoginRequest;
import com.dev6.rejordbe.presentation.controller.dto.login.LoginResponse;
import io.swagger.annotations.*;
import io.swagger.models.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Api(tags = "로그인 컨트롤러")
@RestController
@RequestMapping("/v1/login")
@lombok.RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @ApiOperation(
            value = "로그인",
            nickname = "login",
            notes = "로그인 API.",
            response = LoginResponse.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "로그인 성공",
                    responseHeaders = @ResponseHeader(name = "Set-Cookie", description = "로그인 유저 세션쿠키", response = String.class)
            ),
            @ApiResponse(code = 404, message = "존재하지 않는 유저", response = ErrorResponse.class)
    })
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginResponse> login(
            @ApiParam(value = "로그인 대상 정보", required = true) @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest
    ) {
        UserResult loginResult = loginService.logIn(request.getUserId(), request.getPassword());

        // cookie 설정
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(CookieNames.LOGGED_IN_UID, loginResult.getUid());

        return ResponseEntity.ok(
                LoginResponse.builder()
                        .uid(loginResult.getUid())
                        .userId(loginResult.getUserId())
                        .nickname(loginResult.getNickname())
                        .userType(loginResult.getUserType())
                        .build());
    }
}
