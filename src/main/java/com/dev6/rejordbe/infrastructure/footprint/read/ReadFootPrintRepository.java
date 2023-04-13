package com.dev6.rejordbe.infrastructure.footprint.read;

import com.dev6.rejordbe.domain.footprint.Footprint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReadFootPrintRepository
 */
public interface ReadFootPrintRepository extends JpaRepository<Footprint, String>, ReadFootPrintRepositoryCustom {
}
