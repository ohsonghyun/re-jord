package com.dev6.rejordbe.infrastructure.user

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.user.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * UsersRepositorySpec
 */
@DataJpaTest
@Import(TestConfig.class)
class UsersRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UsersRepository usersRepository

    def "유저를 생성할 수 있다"() {
        given:
        def newUser = Users.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .build()

        when:
        usersRepository.save(newUser)

        entityManager.flush()
        entityManager.clear()

        then:
        Optional<Users> optionalAnUser = usersRepository.findById(userId)
        optionalAnUser.isPresent()
        def anUser = optionalAnUser.orElseThrow()
        anUser.getUserId() == userId
        anUser.getEmail() == email
        anUser.getNickname() == nickname
        anUser.getPassword() == password

        where:
        userId   | email             | nickname   | password
        "userId" | "email@email.com" | "nickname" | "password"
    }

    def "닉네임으로 유저를 찾을 수 있다"() {
        given:
        def newUser = Users.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .password(password)
                .build()

        when:
        usersRepository.save(newUser)

        entityManager.flush()
        entityManager.clear()

        then:
        Optional<Users> optionalAnUser = usersRepository.findUserByNickname(nickname)
        optionalAnUser.isPresent()
        def anUser = optionalAnUser.orElseThrow()
        anUser.getUserId() == userId
        anUser.getEmail() == email
        anUser.getNickname() == nickname
        anUser.getPassword() == password

        where:
        userId   | email             | nickname   | password
        "userId" | "email@email.com" | "nickname" | "password"
    }
}
