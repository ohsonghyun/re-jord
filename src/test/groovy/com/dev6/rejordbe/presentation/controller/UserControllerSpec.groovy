package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.user.signup.SignUpService
import com.dev6.rejordbe.application.user.userinfo.UserInfoService
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.DuplicatedUserIdException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest
import com.dev6.rejordbe.presentation.controller.dto.userInfo.UpdateUserInfoRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
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
    @MockBean
    UserInfoService userInfoService

    private static final String baseUrl = '/v1/users'

    // 회원가입
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
                post(baseUrl)
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
                post(baseUrl)
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
                post(baseUrl)
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

    // /회원가입

    // 닉네임 수정
    def "유저 닉네임 변경에 성공하면 200 반환"() {
        given:
        when(userInfoService.updateUserInfo(isA(Users.class)))
                .thenReturn(
                        UserResult.builder()
                                .uid(uid)
                                .nickname(nickname)
                                .build())

        expect:
        mvc.perform(
                patch(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateUserInfoRequest.builder()
                                        .uid(uid)
                                        .nickname(nickname)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.uid').value(uid))
                .andExpect(jsonPath('\$.nickname').value(nickname))

        where:
        uid   | nickname
        'uid' | 'nickname'
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        when(userInfoService.updateUserInfo(isA(Users.class))).thenThrow(exception)

        expect:
        mvc.perform(
                patch(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateUserInfoRequest.builder()
                                        .uid('uid')
                                        .nickname('nickname')
                                        .build())))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase             | message               | exception                                | resultStatus
        '정책에 맞지 않은 닉네임: 400' | "ILLEGAL_NICKNAME"    | new IllegalParameterException(message)   | status().isBadRequest()
        '이미 존재하는 닉네임: 409'   | "DUPLICATED_NICKNAME" | new DuplicatedNicknameException(message) | status().isConflict()
        '존재하지 않는 유저: 404'    | "USER_NOT_FOUND"      | new UserNotFoundException(message)       | status().isNotFound()
    }
    // /닉네임 수정
}
