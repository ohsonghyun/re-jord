package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UsersRepository
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
}
