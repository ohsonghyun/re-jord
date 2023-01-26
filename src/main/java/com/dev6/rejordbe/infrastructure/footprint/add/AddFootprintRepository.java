package com.dev6.rejordbe.infrastructure.footprint.add;

import com.dev6.rejordbe.domain.footprint.Footprint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AddFootprintRepository
 */
public interface AddFootprintRepository extends JpaRepository<Footprint, String> {
}
