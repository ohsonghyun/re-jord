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
        testCase                 | userId                  | validateResult | errorCount
        'userId가 null이면 에러'   | null                    | false          | 1
        'userId 길이가 4이면 에러'  | 'user'                  | false          | 1
        'userId 길이가 21이면 에러' | 'useruseruseruseruser1' | false          | 1
        'userId 길이가 5이면 성공'  | 'user5'                 | true           | 0
        'userId 길이가 20이면 성공' | 'useruseruseruseruser'  | true           | 0
    }

    def "영문(소문자)만으로는 생성가능하나, 숫자로만 생성은 불가능하다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>()

        when:
        def result = userInfoValidateService.validateUserId(userId, errors)

        then:
        result == validateResult
        errors.size() == errorCount

        where:
        testCase                         | userId        | validateResult | errorCount
        '영문으로만 구성된 경우'             | 'userid'      | true           | 0
        '숫자로만 구성된 경우'               | '12345'       | false          | 1
        '영문(소문자)과 숫자의 조합인 경우'    | 'userid1234'  | true           | 0
        '영문과 숫자 이외의 문자가 포함된 경우' | 'userid$1234' | false          | 1
        '영문(대문자)가 포함된 경우'          | 'userId1234'  | false          | 1
    }

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
        testCase                   | nickname      | validateResult | errorCount
        'nickname이 null이면 에러'   | null          | false          | 1
        'nickname 길이가 1이면 에러'  | 'u'           | false          | 1
        'nickname 길이가 11이면 에러' | 'useruseruse' | false          | 1
        'nickname 길이가 2이면 성공'  | 'us'          | true           | 0
        'nickname 길이가 10이면 성공' | 'useruserus'  | true           | 0
    }

    @Unroll("#testCase")
    def "Nickname은 영문, 한글 또는 숫자로 구성된다"() {
        given:
        List<RuntimeException> errors = new ArrayList<>()

        when:
        def result = userInfoValidateService.validateNickname(nickname, errors)

        then:
        result == validateResult
        errors.size() == errorCount

        where:
        testCase                            | nickname   | validateResult | errorCount
        'nickname 영어 소문자인 경우'          | 'uu'       | true           | 0
        'nickname 영어 대문자인 경우'          | 'UU'       | true           | 0
        'nickname 한글인 경우'                | '한글'      | true           | 0
        'nickname 숫자인 경우'                | '123'      | true           | 0
        'nickname 영어, 한글, 숫자 혼합인 경우' | 'us12US3'  | true           | 0
        'nickname 특수문자가 포함된 경우'       | 'us12U%S3' | false          | 1
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
        testCase                             | password | validateResult | errorCount
        'password가 null이면 에러'             | null     | false          | 1
        'password가 emptyString 이면 에러'     | ''       | false          | 1
        'password가 문자열이 들어가 있으면 성공'  | 'a'      | true           | 0
    }
}
