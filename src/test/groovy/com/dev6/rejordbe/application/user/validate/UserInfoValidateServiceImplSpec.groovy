package com.dev6.rejordbe.application.user.validate

import spock.lang.Specification
import spock.lang.Unroll

/**
 * UserInfoValidateServiceImplSpec
 */
class UserInfoValidateServiceImplSpec extends Specification {

    UserInfoValidateServiceImpl userInfoValidateService = new UserInfoValidateServiceImpl()

    // UserId 관련
    @Unroll("#testCase")
    def "UserId는 5~20자 이내로 구성된다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>()

        when:
        def result = userInfoValidateService.validateUserId(userId, errors)

        then:
        result == validateResult
        errors.size() == errorCount

        where:
        testCase             | userId                  | validateResult | errorCount
        'userId가 null이면 에러'  | null                    | false          | 1
        'userId 길이가 4이면 에러'  | 'user'                  | false          | 1
        'userId 길이가 21이면 에러' | 'useruseruseruseruser1' | false          | 1
        'userId 길이가 5이면 성공'  | 'user5'                 | true           | 0
        'userId 길이가 20이면 성공' | 'useruseruseruseruser'  | true           | 0
    }

//    def "UserId는 영문과 숫자로만 구성된다"() {
//        // TODO
//    }

    // Nickname 관련
    @Unroll("#testCase")
    def "Nickname은 2~15자 이내로 구성된다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>()

        when:
        def result = userInfoValidateService.validateNickname(nickname, errors)

        then:
        result == validateResult
        errors.size() == errorCount

        where:
        testCase               | nickname           | validateResult | errorCount
        'nickname이 null이면 에러'  | null               | false          | 1
        'nickname 길이가 1이면 에러'  | 'u'                | false          | 1
        'nickname 길이가 16이면 에러' | 'useruseruseruser' | false          | 1
        'nickname 길이가 2이면 성공'  | 'us'               | true           | 0
        'nickname 길이가 15이면 성공' | 'useruseruseruse'  | true           | 0
    }

    // Password 관련
    @Unroll("#testCase")
    def "패스워드는 null 또는 emptyString이 아닌 값이다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>()

        when:
        def result = userInfoValidateService.validatePassword(password, errors)

        then:
        result == validateResult
        errors.size() == errorCount

        where:
        testCase                      | password | validateResult | errorCount
        'password가 null이면 에러'         | null     | false          | 1
        'password가 emptyString 이면 에러' | ''       | false          | 1
        'password가 문자열이 들어가 있으면 성공'   | 'a'      | true           | 0
    }
}
