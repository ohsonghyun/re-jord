package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.user.login.LoginService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.login.LoginRequest
import com.dev6.rejordbe.presentation.controller.user.login.LoginController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * LoginControllerSpec
 */
@WebMvcTest(LoginController)
@Import(TestSecurityConfig.class)
class LoginControllerSpec extends Specification {

    private static final String baseUrl = '/v1/login'

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    LoginService loginService

    def "올바른 유저ID와 패스워드라면 로그인 성공: 200"() {
        given:
        when(loginService.logIn(isA(String.class), isA(String.class)))
                .thenReturn(UserResult.builder()
                        .uid(uid)
                        .userId(userId)
                        .nickname(userId)
                        .userType(userType)
                        .build())
        expect:
        mvc.perform(
                post(baseUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer asfsaf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        LoginRequest.builder()
                                                .userId(userId)
                                                .password(password)
                                                .build()
                                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.uid').value(uid))
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.nickname').value(userId))
                .andExpect(jsonPath('\$.userType').value(userType.name()))

        where:
        uid   | userId   | password   | userType
        'uid' | 'userId' | 'password' | UserType.BASIC
    }

    def "잘못된 유저ID와 패스워드라면 로그인 실패: 404"() {
        given:
        when(loginService.logIn(isA(String.class), isA(String.class)))
                .thenThrow(new UserNotFoundException(ExceptionCode.USER_NOT_FOUND.name()))

        expect:
        mvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        LoginRequest.builder()
                                                .userId('userId')
                                                .password('password')
                                                .build()
                                )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.message').value(ExceptionCode.USER_NOT_FOUND.name()))
    }

}
