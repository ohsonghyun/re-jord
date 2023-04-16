package com.dev6.rejordbe.infrastructure.badge;

import com.dev6.rejordbe.domain.badge.BadgeImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BadgeImageRepository
 */
public interface BadgeImageRepository extends JpaRepository<BadgeImage, String> {
}
