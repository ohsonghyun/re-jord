package com.dev6.rejordbe.application.user.login

import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * LogInServiceImplSpec
 */
class LogInServiceImplSpec extends Specification {

    UserInfoRepository userInfoRepository
    LoginService loginService

    def setup() {
        userInfoRepository = Mock(UserInfoRepository.class)
        loginService = new LoginServiceImpl(userInfoRepository)
    }

    def "올바른 userId를 입력하면 유저 정보 획득에 성공한다"() {
        given:
        def userId = 'userId'
        def password = 'password'

        userInfoRepository.findUserByUserId(userId) >> Optional.of(
                Users.builder()
                        .userId(userId)
                        .password(password)
                        .roles(List.of(new Role(RoleType.ROLE_USER)))
                        .build()
        )

        when:
        def loggedInUser = loginService.findUserByUserId(userId)

        then:
        loggedInUser != null
        loggedInUser.getUserId() == userId
        loggedInUser.getPassword() == null // 패스워드는 넘기지 않음
    }

    @Unroll("#testCase")
    def "존재하지 않는 userId를 입력하면 유저정보 획득에 실패한다: 404에러"() {
        given:
        userInfoRepository.findUserByUserId(userId) >> Optional.empty()

        when:
        loginService.findUserByUserId(userId)

        then:
        thrown(UserNotFoundException)

        where:
        testCase            | userId
        '유저ID가 null'        | null
        '유저ID가 emptyString' | ''
        '존재하지 않는 유저ID'      | 'unknownUser'
    }

}
