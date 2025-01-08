package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.transit.domain.model.segment.BusSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusSegmentJpaRepository extends JpaRepository<BusSegment, Long> {

    List<BusSegment> findByTransitRouteId (Long transitRouteId);

}
