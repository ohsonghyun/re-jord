package com.dev6.rejordbe.application.user.signup

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.application.user.validate.UserInfoValidateService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.DuplicatedUserIdException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import spock.lang.Specification

/**
 * SignUpServiceImplSpec
 */
class SignUpServiceImplSpec extends Specification {

    SignUpService signUpService
    SignUpRepository signUpRepository
    UserInfoValidateService userInfoValidateService
    IdGenerator idGenerator

    def setup() {
        signUpRepository = Mock(SignUpRepository.class)
        userInfoValidateService = Mock(UserInfoValidateService.class)
        idGenerator = Mock(IdGenerator.class)
        signUpService = new SignUpServiceImpl(signUpRepository, userInfoValidateService, idGenerator)
    }

    def "에러가 없는 경우 회원가입을 할 수 있다"() {
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()
        signUpRepository.findUserByUserId(_ as String) >> Optional.empty()
        signUpRepository.save(_ as Users) >> Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .userType(userType)
                .build()

        when:
        def saveResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())
        then:
        saveResult.getUid() == uid
        saveResult.getUserId() == userId
        saveResult.getNickname() == nickname
        saveResult.getUserType() == userType
        saveResult.getPassword() == null
        saveResult.getErrors() == null

        where:
        uid | userId   | nickname   | password   | userType
        'uid' | 'userId' | 'nickname' | 'password' | UserType.BASIC
    }

    def "에러가 발생하면 회원가입을 할 수 없다"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())

        when:
        def saveResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())
        then:
        0 * signUpRepository.save(_ as Users)
        saveResult.getErrors().size() == 1

        where:
        userId   | nickname   | password   | userType
        'userId' | 'nickname' | 'password' | UserType.BASIC
    }

    def "동일한 userId가 존재하면 에러 리스트 반환"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())
        signUpRepository.findUserByNickname(_ as String) >> Optional.empty()

        when:
        def userResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == 1
        errors.get(0) asType(DuplicatedUserIdException.class)

        where:
        userId   | nickname   | password   | userType
        'userId' | 'nickname' | 'password' | UserType.BASIC
    }

    def "동일한 nickname이 존재하면 에러 리스트 반환"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true
        userInfoValidateService.validatePassword(_ as String, _ as List<RuntimeException>) >> true
        signUpRepository.findUserByUserId(_ as String) >> Optional.empty()
        signUpRepository.findUserByNickname(_ as String) >> Optional.of(
                Users.builder()
                        .uid('uid')
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())

        when:
        def userResult = signUpService.signUp(
                Users.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == 1
        errors.get(0) asType(DuplicatedNicknameException.class)

        where:
        userId   | nickname   | password   | userType
        'userId' | 'nickname' | 'password' | UserType.BASIC
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
                .userType(userType)
                .build()

        def userResult = signUpService.signUp(anUser)

        then:
        def errors = userResult.getErrors()
        !errors.isEmpty()
        errors.size() == errorSize
        errors.get(0) asType(IllegalParameterException.class)

        where:
        testCase                | userId   | nickname   | password   | userType       | errorSize
        'userId가 누락'            | null     | 'nickname' | 'password' | UserType.BASIC | 1
        'password가 누락'          | 'userId' | 'nickname' | null       | UserType.BASIC | 1
        'userId & password가 누락' | null     | 'nickname' | null       | UserType.BASIC | 2
    }

    def "아이디 중복 체크: 성공한 경우"() {
        // 테스트 대상 클래스는 무엇일까요? -> SignUpServiceImpl
        // 테스트 대상 메서드(함수) 는 무엇? -> checkUserId
        given:
        signUpRepository.findUserByUserId(_ as String) >> returnValue
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true

        when:
        def duplicateCheckedUserId = signUpService.checkDuplicatedUserId(userId)

        then:
        // users 가 저장이 되고 있는지? -> 기존 user에서 userId가 있는지 확인해야 하는건데.. -> 기존유저가 눈으로 보이지 않는다.
        result == duplicateCheckedUserId

        where:
        userId    | returnValue      | result
        'userId'  | Optional.empty() | userId
    }

    def "아이디 중복 체크: 아이디 정책을 만족시키지 못하면 에러"() {
        given:
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> false

        when:
        signUpService.checkDuplicatedUserId('userId')

        then:
        thrown(IllegalParameterException)
    }

    def "아이디 중복 체크: 중복되서 실패한 경우 에러"() {
        given:
        signUpRepository.findUserByUserId(_ as String) >> Optional.of(Users.builder().build())
        userInfoValidateService.validateUserId(_ as String, _ as List<RuntimeException>) >> true

        when:
        signUpService.checkDuplicatedUserId('userId')

        then:
        thrown(DuplicatedUserIdException)

    }

}
