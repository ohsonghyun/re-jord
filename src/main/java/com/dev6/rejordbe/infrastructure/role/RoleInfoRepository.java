package com.dev6.rejordbe.infrastructure.role;

import com.dev6.rejordbe.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * RoleInfoRepository
 */
public interface RoleInfoRepository extends JpaRepository<Role, Long> {
    /**
     * {@code name}으로 {@code Role}검색
     *
     * @param name {@code String} roleName
     * @return {@code Optional<Role>}
     */
    Optional<Role> findByName(final String name);

    /**
     * {@code names}에 있는 {@code Role} 획득
     *
     * @param names {@code List<String>} 획득하고자 하는 ROLE 리스트
     * @return {@code List<Role>}
     */
    List<Role> findByNameIn(final List<String> names);
}
