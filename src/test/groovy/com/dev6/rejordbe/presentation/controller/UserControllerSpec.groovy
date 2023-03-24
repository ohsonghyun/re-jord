package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.user.signup.SignUpService
import com.dev6.rejordbe.application.user.userinfo.UserInfoService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.DuplicatedUserIdException
import com.dev6.rejordbe.exception.IllegalParameterException

import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.signup.SignUpRequest
import com.dev6.rejordbe.presentation.controller.dto.userInfo.UpdateUserInfoRequest
import com.dev6.rejordbe.presentation.controller.user.info.UserController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController)
@Import(TestSecurityConfig.class)
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
        when(signUpService.signUp(isA(Users.class), isA(List.class)))
                .thenReturn(
                        UserResult.builder()
                                .uid(uid)
                                .userId(userId)
                                .nickname(userId)
                                .roles(Collections.singletonList(roleType))
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
                                                .roles(Collections.singletonList(roleType))
                                                .build()
                                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('\$.uid').value(uid))
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.nickname').value(userId))
                .andExpect(jsonPath('\$.roles.size()').value(1))
                .andExpect(jsonPath('\$.roles[0]').value(roleType))

        where:
        uid   | userId   | password   | roleType
        'uid' | 'userId' | 'password' | RoleType.ROLE_USER
    }

    def "정책에 맞지 않은 데이터가 있으면 회원가입에 실패하고 400을 반환한다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>();
        errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID))
        errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME))
        errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_PASSWORD))
        errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_ROLE))

        when(signUpService.signUp(isA(Users.class), isA(List.class)))
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
                                                .roles(Collections.emptyList())
                                                .build()
                                )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.errors.size()').value(4))
                .andExpect(jsonPath('\$.errors[0]').value(ExceptionCode.ILLEGAL_USERID))
                .andExpect(jsonPath('\$.errors[1]').value(ExceptionCode.ILLEGAL_NICKNAME))
                .andExpect(jsonPath('\$.errors[2]').value(ExceptionCode.ILLEGAL_PASSWORD))
                .andExpect(jsonPath('\$.errors[3]').value(ExceptionCode.ILLEGAL_ROLE))
    }

    def "동일한 유저ID 또는 닉네임이 존재하면 회원가입에 실패하고 409을 반환한다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>();
        errors.add(new DuplicatedUserIdException("DUPLICATED_USERID"))
        errors.add(new DuplicatedNicknameException("DUPLICATED_NICKNAME"))

        when(signUpService.signUp(isA(Users.class), isA(List.class)))
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
                                                .roles(Collections.emptyList())
                                                .build()
                                )))
                .andExpect(status().isConflict())
                .andExpect(jsonPath('\$.errors.size()').value(2))
                .andExpect(jsonPath('\$.errors[0]').value(ExceptionCode.DUPLICATED_USERID))
                .andExpect(jsonPath('\$.errors[1]').value(ExceptionCode.DUPLICATED_NICKNAME))
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
                patch(baseUrl + '/uid/nickname')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateUserInfoRequest.builder()
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
                patch(baseUrl + '/uid/nickname')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateUserInfoRequest.builder()
                                        .nickname('nickname')
                                        .build())))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase             | message                           | exception                                | resultStatus
        '정책에 맞지 않은 닉네임: 400' | ExceptionCode.ILLEGAL_NICKNAME    | new IllegalParameterException(message)   | status().isBadRequest()
        '이미 존재하는 닉네임: 409'   | ExceptionCode.DUPLICATED_NICKNAME | new DuplicatedNicknameException(message) | status().isConflict()
        '존재하지 않는 유저: 404'    | ExceptionCode.USER_NOT_FOUND      | new UserNotFoundException(message)       | status().isNotFound()
    }
    // /닉네임 수정

    // 아이디 중복 체크
    def "아이디 중복이 되지 않으면 200을 반환한다"() {
        given:
        when(signUpService.isNotDuplicatedUserId(isA(String.class)))
                .thenReturn('userId')

        expect:
        mvc.perform(get(baseUrl + '/userId/duplication')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.userId').value("userId"))
    }

    def "아이디 중복 실패 케이스: #testCase 반환"() {
        given:
        when(signUpService.isNotDuplicatedUserId(isA(String.class))).thenThrow(exception)

        expect:
        mvc.perform(get(baseUrl + '/userId/duplication')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase             | message             | exception                              | resultStatus
        '정책에 맞지 않은 아이디: 400' | "ILLEGAL_USERID"    | new IllegalParameterException(message) | status().isBadRequest()
        '이미 존재하는 아이디: 409'   | "DUPLICATED_USERID" | new DuplicatedUserIdException(message) | status().isConflict()
    }
    // /아이디 중복 체크

    // 마이페이지
    def "마이페이지 정보 획득 시 200을 반환한다"() {
        given:
        when(userInfoService.findUserInfoByUid(isA(String.class)))
                .thenReturn(UserInfoForMyPage.builder()
                        .badgeAmount(badgeAmount)
                        .totalFootprintAmount(totalFootprintAmount)
                        .nickname(nickname)
                        .dDay(dDay)
                        .build())

        expect:
        mvc.perform(get(baseUrl + '/mypage')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.nickname').value(nickname))
                .andExpect(jsonPath('\$.dday').value(dDay))
                .andExpect(jsonPath('\$.badgeAmount').value(badgeAmount))
                .andExpect(jsonPath('\$.totalFootprintAmount').value(totalFootprintAmount))

        where:
        nickname   | badgeAmount | totalFootprintAmount | uid   | dDay
        'nickname' | 3           | 45                   | 'uid' | 3
    }

    def "마이페이지 정보 획득 실패 케이스: #testCase 반환"() {
        given:
        when(userInfoService.findUserInfoByUid(isA(String.class))).thenThrow(exception)

        expect:
        mvc.perform(get(baseUrl + '/mypage')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase                    | message                  | exception                                | resultStatus
        '존재하지 않는 유저: 404'      | "USER_NOT_FOUND"         | new UserNotFoundException(message)       | status().isNotFound()
    }
    // / 마이페이지
}
