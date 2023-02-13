package com.dev6.rejordbe.presentation.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Header
import com.auth0.jwt.interfaces.JWTPartsParser
import com.auth0.jwt.interfaces.JWTVerifier
import com.auth0.jwt.interfaces.Payload
import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.user.login.LoginService
import com.dev6.rejordbe.application.user.userinfo.UserInfoService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.login.LoginRequest
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest
import com.dev6.rejordbe.presentation.controller.token.TokenController
import com.dev6.rejordbe.presentation.controller.user.login.LoginController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * TokenController
 */
@WebMvcTest(TokenController)
@Import(TestSecurityConfig.class)
class TokenControllerSpec extends Specification {

    private static final String baseUrl = '/v1/token/refresh'

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    UserInfoService userInfoService;
    @MockBean
    JWTVerifier jwtVerifier;

    def "JWT으로부터 취득한 uid가 존재하지 않으면 403에러"() {
        given:
        when(userInfoService.findUserByUid(isA(String.class)))
                .thenReturn(Optional.empty())

        expect:
        mvc.perform(
                get(baseUrl)
                        .header(HttpHeaders.AUTHORIZATION, "refreshToken"))
                .andExpect(status().isForbidden())
    }

    def "기간이 초과된 refreshToken인 경우 403에러"() {
        given:
        when(userInfoService.findUserByUid(isA(String.class)))
                .thenReturn(Optional.of(UserResult.builder().uid('uid').build()))
        when(jwtVerifier.verify(isA(String.class)))
                .thenThrow(new JWTVerificationException("dummy"))

        expect:
        mvc.perform(
                get(baseUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer refreshToken"))
                .andExpect(status().isForbidden())
    }

//    def "새로운 accessToken & refreshToken 을 생성할 수 있다"() {
//        // FIXME DecodedJWT mock 가.. 쉽지 않음.. 나중에 여유 있을 때 다시 알아보기
//    }
}
