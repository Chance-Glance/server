package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.global.common.exception.EntityNotFoundException;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;
import com.example.mohago_nocar.transit.domain.repository.TransitRouteRepository;
import com.example.mohago_nocar.transit.infrastructure.error.exception.TransitRouteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransitRouteRepositoryImpl implements TransitRouteRepository {

    private final TransitRouteJpaRepository transitRouteJpaRepository;

    @Override
    public TransitRoute findByDepartureAndArrival(RoutePoint from, RoutePoint to) {
        List<TransitRoute> transitRoutes = transitRouteJpaRepository.findByDepartureAndArrival(from, to);

        if (transitRoutes.isEmpty()) {
            throw new TransitRouteNotFoundException();
        }

        return transitRoutes.getFirst();
    }

    @Override
    public TransitRoute save(TransitRoute transitRoute) {
        return transitRouteJpaRepository.save(transitRoute);
    }

}
