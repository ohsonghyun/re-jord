package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UsersRepository
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    /**
     * 닉네임으로 유저 찾기
     *
     * @param nickname {@code String} 찾고자 하는 유저의 nickname
     * @return {@code Optional<Users>}
     */
    Optional<Users> findUserByNickname(String nickname);

    /**
     * 유저ID로 유저 찾기
     *
     * @param userId {@code String}
     * @return {@code Optional<Users>}
     */
    Optional<Users> findUserByUserId(String userId);
}
