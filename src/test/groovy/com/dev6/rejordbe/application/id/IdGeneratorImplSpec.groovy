package com.dev6.rejordbe.application.id

import spock.lang.Specification
import spock.lang.Unroll

/**
 * idGeneratorImplSpec
 */
class IdGeneratorImplSpec extends Specification {

    IdGenerator impl

    def setup() {
        impl = new IdGeneratorImpl()
    }

    @Unroll("#testCase")
    def "주어진 prefix를 포함한 무작위 아이디 생성 가능"() {
        when:
        def generatedId = impl.generate(prefix)

        then:
        generatedId.startsWith(prefix + "-")

        where:
        testCase      | prefix
        "prefix - US" | "US"
        "prefix - PS" | "PS"
    }

    @Unroll("#testCase")
    def "주어진 prefix가 null 또는 EmptyString이면 에러"() {
        when:
        impl.generate(prefix)

        then:
        thrown(IllegalArgumentException)

        where:
        testCase               | prefix
        "prefix - null"        | null
        "prefix - EmptyString" | ""
    }
}
