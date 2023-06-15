package com.dev6.rejordbe.presentation.controller.user.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dev6.rejordbe.application.user.login.LoginService;
import com.dev6.rejordbe.config.JwtConfig;
import com.dev6.rejordbe.domain.jwt.JwtToken;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.dev6.rejordbe.presentation.controller.dto.login.LoginRequest;
import com.dev6.rejordbe.presentation.controller.dto.login.LoginResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

import static com.dev6.rejordbe.domain.jwt.Jwt.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Api(tags = "로그인 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/login")
@lombok.RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtConfig jwtConfig;

    @ApiOperation(
            value = "로그인",
            nickname = "login",
            notes = "로그인 API.",
            response = LoginResponse.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "로그인 컨트롤러"
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
        log.info("LoginController.login: LoginRequest: userId: {}", request.getUserId());
        UserResult loginResult = loginService.findUserByUserId(request.getUserId());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginResult.getUid(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = (User)authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSortKey().getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getAccessToken().getPeriod())) // 30분
                .withIssuer(httpServletRequest.getRequestURI())
                .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getRefreshToken().getPeriod())) // 60분
                .withIssuer(httpServletRequest.getRequestURI())
                .sign(algorithm);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, BEARER_WITH_SPACE + accessToken);

        return new ResponseEntity<>(
                LoginResponse.builder()
                        .uid(loginResult.getUid())
                        .userId(loginResult.getUserId())
                        .nickname(loginResult.getNickname())
                        .roles(loginResult.getRoles())
                        .tokens(new JwtToken(accessToken, refreshToken))
                        .build()
                , httpHeaders
                , HttpStatus.OK);
    }
}
