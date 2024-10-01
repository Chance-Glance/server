package com.example.mohago_nocar.course.infrastructure.routeStep;

import com.example.mohago_nocar.course.domain.repository.RouteStepRepository;
import com.example.mohago_nocar.course.infrastructure.routeStep.RouteStepJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RouteStepRepositoryImpl implements RouteStepRepository {

    private RouteStepJpaRepository routeStepJpaRepository;
}
