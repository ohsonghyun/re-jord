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
    RoleInfoRepository roleInfoRepository
    @Autowired
    SignUpRepository signUpRepository

    def "#roleType 유저를 생성할 수 있다"() {
        given:
        def now = LocalDateTime.now()
        def role = roleInfoRepository.save(new Role(roleType))
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(role))
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
        anUser.getRoles().size() == 1
        anUser.getRoles().get(0).getName() == roleType
        anUser.getCreatedDate().isAfter(now)
        anUser.getModifiedDate().isAfter(now)

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_ADMIN
    }

    @Unroll
    def "#testCase 가입에 실패한다"() {
        given:
        def role = roleInfoRepository.save(new Role(RoleType.ROLE_USER))

        when:
        // 닉네임이 'nickname' 인 첫 번째 유저 등록 -> 성공
        signUpRepository.save(Users.builder()
                .uid('uid')
                .userId('userId')
                .nickname('nickname')
                .password('password')
                .roles(Collections.singletonList(role))
                .build())
        entityManager.flush()
        entityManager.clear()

        // 닉네임이 'nickname' 인 첫 번째 유저 등록 -> 실패
        signUpRepository.save(Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password('password')
                .roles(Collections.singletonList(role))
                .build())
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
        def role = roleInfoRepository.save(new Role(roleType))
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(role))
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
        anUser.getRoles().size() == 1
        anUser.getRoles().get(0).getName() == roleType

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

    def "유저ID로 유저를 찾을 수 있다"() {
        given:
        def role = roleInfoRepository.save(new Role(roleType))
        def newUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(role))
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
        anUser.getRoles().size() == 1
        anUser.getRoles().get(0).getName() == roleType

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

}
