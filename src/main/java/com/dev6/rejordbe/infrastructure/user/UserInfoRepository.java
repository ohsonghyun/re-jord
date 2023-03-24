package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserInfoRepository
 */
@Repository
public interface UserInfoRepository extends JpaRepository<Users, String>, UserInfoRepositoryCustom {

    /**
     * 유저ID로 유저 찾기
     *
     * @param userId {@code String} 찾고자 하는 유저의 userId
     * @return {@code Optional<Users>}
     */
    Optional<Users> findUserByUserId(final String userId);

    /**
     * 닉네임으로 유저 찾기
     *
     * @param nickname {@code String} 찾고자 하는 유저의 nickname
     * @return {@code Optional<Users>}
     */
    Optional<Users> findUserByNickname(final String nickname);

    /**
     * UID로 유저 찾기
     *
     * @param uid {@code String}
     * @return {@code Optional<Users>}
     */
    Optional<Users> findUserByUid(String uid);
}
