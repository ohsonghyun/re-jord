package com.dev6.rejordbe.infrastructure.user

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.infrastructure.role.RoleInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * UserInfoRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class UserInfoRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RoleInfoRepository roleInfoRepository
    @Autowired
    UserInfoRepository userInfoRepository

    // ----------------------------------------------------
    // 로그인 관련
    // ----------------------------------------------------

    def "UserId로 유저를 검색할 수 있다"() {
        def role = roleInfoRepository.save(new Role(roleType))
        userInfoRepository.save(
                Users.builder()
                        .uid(uid)
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(role))
                        .build())
        entityManager.flush()
        entityManager.clear()

        when:
        def anUser = userInfoRepository.findUserByUserId(userId).orElseThrow()

        then:
        anUser != null
        anUser.getUid() == uid
        anUser.getUserId() == userId
        anUser.getNickname() == nickname
        anUser.getPassword() == password
        anUser.getRoles().size() == 1
        anUser.getRoles().get(0).getName() == roleType

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

    def "유저ID가 null인 경우에도 에러를 반환하지 않는다"() {
        given:
        def role = roleInfoRepository.save(new Role(roleType))
        userInfoRepository.save(
                Users.builder()
                        .uid('uid')
                        .userId('userId')
                        .nickname('nickname')
                        .password('password')
                        .roles(Collections.singletonList(role))
                        .build())
        entityManager.flush()
        entityManager.clear()

        when:
        def userOptional = userInfoRepository.findUserByUserId(userId)

        then:
        userOptional.isEmpty()

        where:
        testCase             | uid   | userId   | nickname   | password   | roleType
        "userId가 null인 경우"   | "uid" | null     | "nickname" | "password" | RoleType.ROLE_USER
    }

    // ----------------------------------------------------
    // 닉네임 수정 관련
    // ----------------------------------------------------

    def "유저 닉네임을 수정할 수 있다"() {
        given: "유저 생성"
        def role = roleInfoRepository.save(new Role(roleType))
        userInfoRepository.save(
                Users.builder()
                        .uid(uid)
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(role))
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
        resultUserInfo.getModifiedDate().isAfter(resultUserInfo.getCreatedDate())

        where:
        uid   | userId   | nickname   | password   | roleType           | newNickname
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER | "newNickname"
    }

}
