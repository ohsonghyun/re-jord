package com.dev6.rejordbe.infrastructure.role

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import org.assertj.core.api.Assertions
import org.assertj.core.api.ListAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * RoleInfoRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class RoleInfoRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    RoleInfoRepository roleInfoRepository

    def "Role 이름으로 Role을 검색할 수 있다"() {
        given:
        roleInfoRepository.save(new Role(RoleType.ROLE_USER))
        entityManager.flush()
        entityManager.clear()

        when:
        def role = roleInfoRepository.findByName(RoleType.ROLE_USER).orElseThrow()

        then:
        role.getId() != null
        role.getName() == RoleType.ROLE_USER
    }

    def "Role 리스트로 복수의 Role을 검색할 수 있다"() {
        given:
        roleInfoRepository.save(new Role(RoleType.ROLE_USER))
        roleInfoRepository.save(new Role(RoleType.ROLE_ADMIN))
        entityManager.flush()
        entityManager.clear()

        when:
        def roles = roleInfoRepository.findByNameIn(List.of(RoleType.ROLE_USER, RoleType.ROLE_ADMIN))

        then:
        !roles.isEmpty()
        roles.size() == 2
        Assertions.assertThat(roles)
                .extractingResultOf('getName')
                .containsExactly(RoleType.ROLE_USER, RoleType.ROLE_ADMIN)
    }
}
