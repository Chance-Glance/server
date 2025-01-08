package com.example.mohago_nocar.transit.infrastructure.repository;

import com.example.mohago_nocar.transit.domain.model.segment.BusSegment;
import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.model.segment.SubwaySegment;
import com.example.mohago_nocar.transit.domain.model.segment.WalkingSegment;
import com.example.mohago_nocar.transit.domain.repository.RouteSegmentRepository;
import com.example.mohago_nocar.transit.infrastructure.error.exception.RouteSegmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class RouteSegmentRepositoryImpl implements RouteSegmentRepository {

    private final WalkingSegmentJpaRepository walkingSegmentJpaRepository;
    private final BusSegmentJpaRepository busSegmentJpaRepository;
    private final SubwayJpaRepository subwayJpaRepository;

    @Override
    public List<RouteSegment> findByTransitRouteId(Long transitRouteId) {
        List<RouteSegment> routeSegments = Stream.of(
                        walkingSegmentJpaRepository.findByTransitRouteId(transitRouteId).stream(),
                        busSegmentJpaRepository.findByTransitRouteId(transitRouteId).stream(),
                        subwayJpaRepository.findByTransitRouteId(transitRouteId).stream()
                )
                .flatMap(stream -> stream)
                .collect(Collectors.toList());

        if (routeSegments.isEmpty()) {
            throw new RouteSegmentNotFoundException();
        }

        return routeSegments;
    }

    @Override
    public WalkingSegment saveWalkingSegment(WalkingSegment walkingSegment) {
        return walkingSegmentJpaRepository.save(walkingSegment);
    }

    @Override
    public void saveAll(List<RouteSegment> routeSegments) {
        saveWalkingSegments(routeSegments);
        saveSubwaySegments(routeSegments);
        saveBusSegments(routeSegments);
    }

    private void saveWalkingSegments(List<RouteSegment> routeSegments) {
        List<WalkingSegment> walkingSegments = routeSegments.stream()
                .filter(routeSegment -> routeSegment instanceof WalkingSegment)
                .map(routeSegment -> (WalkingSegment) routeSegment)
                .toList();

        if (!walkingSegments.isEmpty()) {
            walkingSegmentJpaRepository.saveAll(walkingSegments);
        }
    }

    private void saveSubwaySegments(List<RouteSegment> routeSegments) {
        List<SubwaySegment> subwaySegments = routeSegments.stream()
                .filter(routeSegment -> routeSegment instanceof SubwaySegment)
                .map(routeSegment -> (SubwaySegment) routeSegment)
                .toList();

        if (!subwaySegments.isEmpty()) {
            subwayJpaRepository.saveAll(subwaySegments);
        }
    }

    private void saveBusSegments(List<RouteSegment> routeSegments) {
        List<BusSegment> busSegments = routeSegments.stream()
                .filter(routeSegment -> routeSegment instanceof BusSegment)
                .map(routeSegment -> (BusSegment) routeSegment)
                .toList();

        if (!busSegments.isEmpty()) {
            busSegmentJpaRepository.saveAll(busSegments);
        }
    }

}
