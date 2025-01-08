package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.transit.domain.model.segment.WalkingSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkingSegmentJpaRepository extends JpaRepository<WalkingSegment, Long> {

    List<WalkingSegment> findByTransitRouteId (Long transitRouteId);

}
