package com.dev6.rejordbe.infrastructure.user

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * UserInfoRepositorySpec
 */
@DataJpaTest
@Import(TestConfig.class)
class UserInfoRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserInfoRepository userInfoRepository

    def "유저 닉네임을 수정할 수 있다"() {
        given: "유저 생성"
        userInfoRepository.save(
                Users.builder()
                        .uid(uid)
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .userType(userType)
                        .build())
        entityManager.flush()
        entityManager.clear()

        when:
        def anUser = userInfoRepository.findById(uid).orElseThrow()
        anUser.update(
                Users.builder()
                        .userId("newUserId")
                        .nickname(newNickname)
                        .build())
        entityManager.flush()
        entityManager.clear()

        then:
        def resultUserInfo = userInfoRepository.findById(uid).orElseThrow()
        resultUserInfo.getUserId() != "newUserId" // userId는 변경 불가
        resultUserInfo.getNickname() == newNickname

        where:
        uid   | userId   | nickname   | password   | userType       | newNickname
        "uid" | "userId" | "nickname" | "password" | UserType.BASIC | "newNickname"
    }

}
