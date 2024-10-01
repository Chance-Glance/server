package com.example.mohago_nocar.course.infrastructure.routeStep;

import com.example.mohago_nocar.course.domain.model.routeStep.RouteStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStepJpaRepository extends JpaRepository<RouteStep, Long> {
}
