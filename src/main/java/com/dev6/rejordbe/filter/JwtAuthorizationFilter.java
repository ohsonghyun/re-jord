package com.dev6.rejordbe.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dev6.rejordbe.config.JwtConfig;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.dev6.rejordbe.domain.jwt.Jwt.BEARER_WITH_SPACE;
import static com.dev6.rejordbe.domain.jwt.Jwt.ROLES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * JwtAuthorizationFilter
 */
@Slf4j
@lombok.RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private static final String TOKEN_REFRESH_URL = "/v1/token/refresh";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 토큰 갱신은 Bearer 토큰에 refreshToken 을 설정하기 때문에 제외
        if (request.getServletPath().equals(TOKEN_REFRESH_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Authorization Header 획득
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        // Bearer 토큰 타입 확인
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_WITH_SPACE)) {
            try {
                // JWT 추출
                String token = authorizationHeader.substring(BEARER_WITH_SPACE.length());
                // JWT decode
                Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSortKey());
                JWTVerifier verifier = JWT.require(algorithm).build();
                // JWT 검증
                DecodedJWT decodedJWT = verifier.verify(token);

                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim(ROLES).asArray(String.class);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("JwtAuthorizationFilter.doFilterInternal: JWT_AUTH_ERROR {}", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        ErrorResponse.builder()
                                .message(e.getMessage())
                                .build());
            }
        }
        // Bearer 토큰 확인 실패
        else {
            filterChain.doFilter(request, response);
        }
    }
}
