package com.dev6.rejordbe.infrastructure.user

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.user.UserType
import com.dev6.rejordbe.domain.user.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceException
import java.time.LocalDateTime

/**
 * UsersRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class SignUpRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SignUpRepository signUpRepository

    def "#userType 유저를 생성할 수 있다"() {
        given:
        def now = LocalDateTime.now()
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .userType(userType)
                .build()

        when:
        signUpRepository.save(newUser)

        entityManager.flush()
        entityManager.clear()

        then:
        Optional<Users> optionalAnUser = signUpRepository.findById(uid)
        optionalAnUser.isPresent()
        def anUser = optionalAnUser.orElseThrow()
        anUser.getUid() == uid
        anUser.getUserId() == userId
        anUser.getNickname() == nickname
        anUser.getPassword() == password
        anUser.getUserType() == userType
        anUser.getCreatedDate().isAfter(now)
        anUser.getModifiedDate().isAfter(now)

        where:
        uid   | userId   | nickname   | password   | userType
        "uid" | "userId" | "nickname" | "password" | UserType.BASIC
        "uid" | "userId" | "nickname" | "password" | UserType.ADMIN
    }

    @Unroll
    def "#testCase 가입에 실패한다"() {
        given:
        def newUser1 = Users.builder()
                .uid('uid')
                .userId('userId')
                .nickname('nickname')
                .password('password')
                .userType(UserType.BASIC)
                .build()

        def newUser2 = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password('password')
                .userType(UserType.BASIC)
                .build()

        when:
        // 닉네임이 'nickname' 인 첫 번째 유저 등록 -> 성공
        signUpRepository.save(newUser1)
        entityManager.flush()
        entityManager.clear()

        // 닉네임이 'nickname' 인 첫 번째 유저 등록 -> 실패
        signUpRepository.save(newUser2)
        entityManager.flush()
        entityManager.clear()

        then:
        thrown(PersistenceException)

        where:
        testCase           | uid    | userId    | nickname
        '동일 닉네임이 존재하는 경우'  | 'uid2' | 'userId2' | 'nickname'
        '동일 유저ID가 존재하는 경우' | 'uid2' | 'userId'  | 'nickname2'
    }

    def "닉네임으로 유저를 찾을 수 있다"() {
        given:
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .userType(userType)
                .build()

        when:
        signUpRepository.save(newUser)

        entityManager.flush()
        entityManager.clear()

        then:
        Optional<Users> optionalAnUser = signUpRepository.findUserByNickname(nickname)
        optionalAnUser.isPresent()
        def anUser = optionalAnUser.orElseThrow()
        anUser.getUid() == uid
        anUser.getUserId() == userId
        anUser.getNickname() == nickname
        anUser.getPassword() == password
        anUser.getUserType() == userType

        where:
        uid   | userId   | nickname   | password   | userType
        "uid" | "userId" | "nickname" | "password" | UserType.BASIC
    }

    def "유저ID로 유저를 찾을 수 있다"() {
        given:
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .userType(userType)
                .build()

        when:
        signUpRepository.save(newUser)

        entityManager.flush()
        entityManager.clear()

        then:
        Optional<Users> optionalAnUser = signUpRepository.findUserByUserId(userId)
        optionalAnUser.isPresent()
        def anUser = optionalAnUser.orElseThrow()
        anUser.getUid() == uid
        anUser.getUserId() == userId
        anUser.getNickname() == nickname
        anUser.getPassword() == password
        anUser.getUserType() == userType

        where:
        uid   | userId   | nickname   | password   | userType
        "uid" | "userId" | "nickname" | "password" | UserType.BASIC
    }

}
