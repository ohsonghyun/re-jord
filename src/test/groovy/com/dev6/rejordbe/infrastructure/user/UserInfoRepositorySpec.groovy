package com.dev6.rejordbe.infrastructure.user

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.post.delete.DeletePostRepository
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
    @Autowired
    DeletePostRepository deletePostRepository

    // ----------------------------------------------------
    // 유저 정보 관련
    // ----------------------------------------------------

    def "UID로 유저를 검색할 수 있다"() {
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
        def anUser = userInfoRepository.findUserByUid(uid).orElseThrow()

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

    // ----------------------------------------------------
    // 마이페이지 관련
    // ----------------------------------------------------

    def "uid가 일치하는 마이페이지 유저 정보를 반환한다"() {
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
        def userInfo = userInfoRepository.searchUserInfoByUid(uid).orElseThrow()

        then:
        userInfo.getNickname() == nickname
        userInfo.getCreatedDate() != null

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

    def "유저uid가 null인 경우에 에러를 반환한다"() {
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
        userInfoRepository.searchUserInfoByUid(uid)

        then:
        thrown(IllegalParameterException)

        where:
        testCase          | uid  | userId   | nickname   | password   | roleType
        "uid가 null인 경우" | null | 'userId' | "nickname" | "password" | RoleType.ROLE_USER
    }

    // ----------------------------------------------------
    // 유저 삭제 관련
    // ----------------------------------------------------
    def "유저를 삭제할 수 있다"() {
        given:
        def role = roleInfoRepository.save(new Role(roleType))
        def user = userInfoRepository.save(
                Users.builder()
                        .uid(uid)
                        .userId(userId)
                        .nickname(nickname)
                        .password(password)
                        .roles(Collections.singletonList(role))
                        .build())

        def newPost = Post.builder()
                .postId('postId')
                .contents('contents')
                .postType(PostType.SHARE)
                .user(user)
                .build()
        deletePostRepository.save(newPost)

        entityManager.flush()
        entityManager.clear()

        when:
        userInfoRepository.deleteById(uid)

        entityManager.flush()
        entityManager.clear()

        then:
        def userOptional = userInfoRepository.findById(uid)
        def postOptional = deletePostRepository.findById(newPost.getPostId())
        postOptional == Optional.empty()
        userOptional == Optional.empty()

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

}
