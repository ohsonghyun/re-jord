package com.dev6.rejordbe.infrastructure.badge.read;

import com.dev6.rejordbe.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReadBadgeRepository
 */
public interface ReadBadgeRepository extends JpaRepository<Badge, String>, ReadBadgeRepositoryCustom {
}
