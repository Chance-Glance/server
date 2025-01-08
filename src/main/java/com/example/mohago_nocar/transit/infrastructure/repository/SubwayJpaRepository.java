package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.transit.domain.model.segment.SubwaySegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubwayJpaRepository extends JpaRepository<SubwaySegment, Long> {

    List<SubwaySegment> findByTransitRouteId (Long transitRouteId);

}
