package com.dev6.rejordbe.application.user.signup

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.application.user.validate.UserInfoValidateService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.DuplicatedUserIdException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.role.RoleInfoRepository
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import spock.lang.Specification

/**
 * SignUpServiceImplSpec
 */
class SignUpServiceImplSpec extends Specification {

    SignUpService signUpService
    SignUpRepository signUpRepository
    RoleInfoRepository roleInfoRepository
    UserInfoValidateService userInfoValidateService
    IdGenerator idGenerator

    def setup() {
        signUpRepository = Mock(SignUpRepository.class)
        roleInfoRepository = Mock(RoleInfoRepository.class)
        userInfoValidateService = Mock(UserInfoValidateService.class)
        idGenerator = Mock(IdGenerator.class)
        signUpService = new SignUpServiceImpl(signUpRepository, roleInfoRepository, userInfoValidateService, idGenerator)
    }

    def "에러가 없는 경우 회원가입을 할 수 있다"() {
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validateRoleTypes(_ as List, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()
        signUpRepository.findUserByUserId(_ as String) >> Optional.empty()
        signUpRepository.save(_ as Users) >> Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(new Role(roleType)))
                .build()

        when:
        def saveResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .build(),
                Collections.singletonList(roleType)
        )
        then:
        saveResult.getUid() == uid
        saveResult.getUserId() == userId
        saveResult.getNickname() == nickname
        saveResult.getRoles().size() == 1
        saveResult.getRoles().get(0) == roleType
        saveResult.getPassword() == null
        saveResult.getErrors() == null

        where:
        uid   | userId   | nickname   | password   | roleType
        'uid' | 'userId' | 'nickname' | 'password' | RoleType.ROLE_USER
    }

    def "에러가 발생하면 회원가입을 할 수 없다"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validateRoleTypes(_ as List, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(new Role(roleType)))
                        .build())

        when:
        def saveResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .build(),
                Collections.singletonList(roleType))
        then:
        0 * signUpRepository.save(_ as Users)
        // 중복 유저 ID
        saveResult.getErrors().size() == 1

        where:
        userId   | nickname   | password   | roleType
        'userId' | 'nickname' | 'password' | RoleType.ROLE_USER
    }

    def "동일한 userId가 존재하면 에러 리스트 반환"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validateRoleTypes(_ as List, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(new Role(roleType)))
                        .build())
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()

        when:
        def userResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .build(),
                Collections.singletonList(roleType))

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == 1
        errors.get(0) asType(DuplicatedUserIdException.class)

        where:
        userId   | nickname   | password   | roleType
        'userId' | 'nickname' | 'password' | RoleType.ROLE_USER
    }

    def "동일한 nickname이 존재하면 에러 리스트 반환"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validateRoleTypes(_ as List, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByUserId(_ as String) >> Optional.empty()
        signUpRepository.findUserByNickname(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(new Role(roleType)))
                        .build())

        when:
        def userResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .build(),
                Collections.singletonList(roleType))

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == 1
        errors.get(0) asType(DuplicatedNicknameException.class)

        where:
        userId   | nickname   | password   | roleType
        'userId' | 'nickname' | 'password' | RoleType.ROLE_USER
    }

    def "필수 입력값 #testCase이면 에러 리스트 반환"() {
        given:
        userInfoValidateService.validateUserId(null, _ as List<RuntimeException>) >> {
            String userIds, List<RuntimeException> list ->
                list.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID.name()))
                return false
        }
        userInfoValidateService.validatePassword(null, _ as List<RuntimeException>) >> {
            String passwords, List<RuntimeException> list ->
                list.add(new IllegalParameterException(ExceptionCode.ILLEGAL_PASSWORD.name()))
                return false
        }
        signUpRepository.findUserByUserId(_ as String) >> Optional.empty()
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()

        when:
        def anUser = Users.builder()
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .build()

        def userResult = signUpService.signUp(anUser, Collections.singletonList(roleType))

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == errorSize
        errors.get(0) asType(IllegalParameterException.class)

        where:
        testCase                | userId   | nickname   | password   | roleType           | errorSize
        'userId가 누락'            | null     | 'nickname' | 'password' | RoleType.ROLE_USER | 1
        'password가 누락'          | 'userId' | 'nickname' | null       | RoleType.ROLE_USER | 1
        'userId & password가 누락' | null     | 'nickname' | null       | RoleType.ROLE_USER | 2
    }

    def "아이디 중복 체크: 성공한 경우"() {
        given:
        signUpRepository.findUserByUserId(_ as String) >> returnValue
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true

        when:
        def duplicateCheckedUserId = signUpService.isNotDuplicatedUserId(userId)

        then:
        result == duplicateCheckedUserId

        where:
        userId   | returnValue      | result
        'userId' | Optional.empty() | userId
    }

    def "아이디 중복 체크: 아이디 정책을 만족시키지 못하면 에러"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> false

        when:
        signUpService.isNotDuplicatedUserId('userId')

        then:
        thrown(IllegalParameterException)
    }

    def "아이디 중복 체크: 중복되서 실패한 경우 에러"() {
        given:
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(Users.builder().build())
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true

        when:
        signUpService.isNotDuplicatedUserId('userId')

        then:
        thrown(DuplicatedUserIdException)
    }

}
