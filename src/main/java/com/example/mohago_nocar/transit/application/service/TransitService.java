package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.application.mapper.TransitMapper;
import com.example.mohago_nocar.transit.domain.model.*;
import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.repository.RouteSegmentRepository;
import com.example.mohago_nocar.transit.domain.repository.TransitRouteRepository;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import com.example.mohago_nocar.transit.infrastructure.externalApi.ODsayApiClient;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.RouteResponseDto;
import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransitService implements TransitUseCase {

    private final ODsayApiClient oDsayApiClient;

    @Override
    public TransitInfo findRouteTransitBetweenPlaces(Location from, Location to) {
        RouteResponseDto response = oDsayApiClient.searchRoute(
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude()
        );

        return TransitMapper.mapRouteResponseDtoToTransitInfo(response);
    @Transactional
    @Override
    public void saveTransitRouteWithSegments(ODsayApiSuccessResponseWrapper response) {
        TransitRouteWithSegments transitRouteWithSegments = mapTransitRouteWithSegments(response);
        TransitRoute transitRoute = saveTransitRoute(transitRouteWithSegments);
        saveRouteSegments(transitRouteWithSegments, transitRoute);
    }

    private void saveRouteSegments(TransitRouteWithSegments transitRouteWithSegments, TransitRoute transitRoute) {
        List<RouteSegment> routeSegments = transitRouteWithSegments.getRouteSegments();
        routeSegments.forEach(routeSegment -> routeSegment.setTransitId(transitRoute.getId()));
        routeSegmentRepository.saveAll(routeSegments);
    }

    private TransitRoute saveTransitRoute(TransitRouteWithSegments transitRouteWithSegments) {
        TransitRoute transitRoute = transitRouteWithSegments.getTransitRoute();
        return transitRouteRepository.save(transitRoute);
    }

    private TransitRouteWithSegments mapTransitRouteWithSegments(ODsayApiSuccessResponseWrapper response) {
        return TransitMapper.mapOdsayApiResponseToTransitRouteWithSegments(
                response.apiResponse(), response.departure(), response.arrival());
    }

}
