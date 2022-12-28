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

    // ----------------------------------------------------
    // 로그인 관련
    // ----------------------------------------------------

    def "유저를 UserId와 패스워드를 통해 검색할 수 있다"() {
        given:
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
        def anUser = userInfoRepository.findUserByUserIdAndPassword(userId, password).orElseThrow()

        then:
        anUser != null
        anUser.getUid() == uid
        anUser.getUserId() == userId
        anUser.getNickname() == nickname
        anUser.getPassword() == password
        anUser.getUserType() == userType

        where:
        uid   | userId   | nickname   | password   | userType
        "uid" | "userId" | "nickname" | "password" | UserType.BASIC
    }

    def "유저를 UserId 또는 패스워드가 null인 경우에도 검색은 가능하다"() {
        given:
        userInfoRepository.save(
                Users.builder()
                        .uid('uid')
                        .userId('userId')
                        .nickname('nickname')
                        .password('password')
                        .userType(userType)
                        .build())
        entityManager.flush()
        entityManager.clear()

        when:
        def userOptional = userInfoRepository.findUserByUserIdAndPassword(userId, password)

        then:
        userOptional.isEmpty()

        where:
        testCase             | uid   | userId   | nickname   | password   | userType
        "userId가 null인 경우"   | "uid" | null     | "nickname" | "password" | UserType.BASIC
        "password가 null인 경우" | "uid" | "userId" | "nickname" | null       | UserType.BASIC
    }

    // ----------------------------------------------------
    // 닉네임 수정 관련
    // ----------------------------------------------------

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
