package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransitRouteJpaRepository extends JpaRepository<TransitRoute, Long> {

    List<TransitRoute> findByDepartureAndArrival(RoutePoint from, RoutePoint to);

}
