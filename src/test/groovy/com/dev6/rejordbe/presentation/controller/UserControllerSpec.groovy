package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.user.signup.SignUpService
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.DuplicatedUserIdException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController)
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    SignUpService signUpService

    def "회원가입 성공시 201을 반환한다"() {
        given:
        when(signUpService.signUp(isA(Users.class)))
                .thenReturn(
                        UserResult.builder()
                                .uid(uid)
                                .userId(userId)
                                .nickname(userId)
                                .userType(userType)
                                .build())

        expect:
        mvc.perform(
                post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        SignUpRequest.builder()
                                                .userId(userId)
                                                .password(password)
                                                .userType(userType)
                                                .build()
                                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('\$.uid').value(uid))
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.nickname').value(userId))
                .andExpect(jsonPath('\$.userType').value(userType.name()))

        where:
        uid   | userId   | password   | userType
        'uid' | 'userId' | 'password' | UserType.BASIC
    }

    def "정책에 맞지 않은 데이터가 있으면 회원가입에 실패하고 400을 반환한다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>();
        errors.add(new IllegalParameterException("ILLEGAL_USERID"))
        errors.add(new IllegalParameterException("ILLEGAL_NICKNAME"))
        errors.add(new IllegalParameterException("ILLEGAL_PASSWORD"))

        when(signUpService.signUp(isA(Users.class)))
                .thenReturn(
                        UserResult.builder()
                                .errors(errors)
                                .build())

        expect:
        mvc.perform(
                post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        SignUpRequest.builder()
                                                .userId('')
                                                .password('')
                                                .userType(null)
                                                .build()
                                )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.errors.size()').value(3))
                .andExpect(jsonPath('\$.errors[0]').value('ILLEGAL_USERID'))
                .andExpect(jsonPath('\$.errors[1]').value('ILLEGAL_NICKNAME'))
                .andExpect(jsonPath('\$.errors[2]').value('ILLEGAL_PASSWORD'))
    }

    def "동일한 유저ID 또는 닉네임이 존재하면 회원가입에 실패하고 409을 반환한다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>();
        errors.add(new DuplicatedUserIdException("DUPLICATED_USERID"))
        errors.add(new DuplicatedNicknameException("DUPLICATED_NICKNAME"))

        when(signUpService.signUp(isA(Users.class)))
                .thenReturn(
                        UserResult.builder()
                                .errors(errors)
                                .build())

        expect:
        mvc.perform(
                post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        SignUpRequest.builder()
                                                .userId('')
                                                .password('')
                                                .userType(null)
                                                .build()
                                )))
                .andExpect(status().isConflict())
                .andExpect(jsonPath('\$.errors.size()').value(2))
                .andExpect(jsonPath('\$.errors[0]').value('DUPLICATED_USERID'))
                .andExpect(jsonPath('\$.errors[1]').value('DUPLICATED_NICKNAME'))
    }

}
