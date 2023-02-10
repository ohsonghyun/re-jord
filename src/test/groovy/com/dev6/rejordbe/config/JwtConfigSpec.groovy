package com.dev6.rejordbe.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class JwtConfigSpec extends Specification {

    @Autowired
    JwtConfig jwtConfig

    def "JWT 설정값을 획득할 수 있다"() {
        expect:
        jwtConfig.getSortKey() == 'sort'
        jwtConfig.getAccessToken().getPeriod() == 1800000
        jwtConfig.getRefreshToken().getPeriod() == 3600000
    }

}
