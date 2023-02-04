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

    def "올바른 userId와 password를 입력하면 로그인 성공한다"() {
        given:
        def userId = 'userId'
        def password = 'password'

        userInfoRepository.findUserByUserIdAndPassword(userId, password) >> Optional.of(
                Users.builder()
                        .userId(userId)
                        .password(password)
                        .roles(List.of(new Role(RoleType.ROLE_USER)))
                        .build()
        )

        when:
        def loggedInUser = loginService.logIn(userId, password)

        then:
        loggedInUser != null
        loggedInUser.getUserId() == userId
        loggedInUser.getPassword() == null // 패스워드는 넘기지 않음
    }

    @Unroll("#testCase")
    def "존재하지 않는 userId와 password를 입력하면 로그인 실패한다: 404에러"() {
        given:
        userInfoRepository.findUserByUserIdAndPassword(userId, password) >> Optional.empty()

        when:
        loginService.logIn(userId, password)

        then:
        thrown(UserNotFoundException)

        where:
        testCase            | userId        | password
        '유저ID가 null'        | null          | 'password'
        '유저ID가 emptyString' | ''            | 'password'
        '패스워드가 null'        | 'userId'      | null
        '패스워드가 emptyString' | 'userId'      | ''
        '존재하지 않는 유저ID'      | 'unknownUser' | 'password'
        '틀린 패스워드'           | 'userId'      | 'wrongPassword'
    }

}
