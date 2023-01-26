package com.dev6.rejordbe.infrastructure.badge.add;

import com.dev6.rejordbe.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AddBadgeRepository
 */
public interface AddBadgeRepository extends JpaRepository<Badge, String> {
}
