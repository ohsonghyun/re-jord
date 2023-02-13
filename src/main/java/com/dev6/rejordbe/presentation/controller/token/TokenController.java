package com.dev6.rejordbe.presentation.controller.token;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dev6.rejordbe.application.user.userinfo.UserInfoService;
import com.dev6.rejordbe.config.JwtConfig;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.IllegalAccessException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.dev6.rejordbe.presentation.controller.dto.token.TokenResponse;
import io.swagger.annotations.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.dev6.rejordbe.domain.jwt.Jwt.BEARER_WITH_SPACE;
import static com.dev6.rejordbe.domain.jwt.Jwt.ROLES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * TokenController
 */
@Api(tags = "토큰 컨트롤러")
@RestController
@RequestMapping("/v1/token")
@lombok.RequiredArgsConstructor
public class TokenController {

    private final JwtConfig jwtConfig;
    private final UserInfoService userInfoService;

    @ApiOperation(
            value = "리프레시 토큰",
            nickname = "refreshToken",
            notes = "리프레시 토큰 API.",
            response = TokenResponse.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "토큰 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "토큰 갱신 성공"),
            @ApiResponse(code = 403, message = "올바르지 않은 토큰", response = ErrorResponse.class)
    })
    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_WITH_SPACE)) {
            throw new IllegalAccessException(ExceptionCode.ILLEGAL_ACCESS.name());
        }

        String refreshToken = authorizationHeader.substring(BEARER_WITH_SPACE.length());
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSortKey().getBytes());

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(refreshToken);
        } catch (JWTVerificationException ex) {
            throw new IllegalAccessException(ex.getMessage());
        }

        String uid = decodedJWT.getSubject();
        UserResult userResult = userInfoService.findUserByUid(uid)
                .orElseThrow(() -> new IllegalAccessException(ExceptionCode.ILLEGAL_ACCESS.name()));

        String accessToken = JWT.create()
                .withSubject(userResult.getUid())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getAccessToken().getPeriod()))
                .withIssuer(request.getRequestURI())
                .withClaim(ROLES, userResult.getRoles())
                .sign(algorithm);

        String newRefreshToken = JWT.create()
                .withSubject(userResult.getUid())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getRefreshToken().getPeriod()))
                .withIssuer(request.getRequestURI())
                .sign(algorithm);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, BEARER_WITH_SPACE + accessToken);

        return new ResponseEntity<>(
                TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build()
                , httpHeaders
                , HttpStatus.OK);
    }
}
