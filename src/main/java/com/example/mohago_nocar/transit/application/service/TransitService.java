package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.transit.application.mapper.TransitMapper;
import com.example.mohago_nocar.transit.domain.model.*;
import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.repository.RouteSegmentRepository;
import com.example.mohago_nocar.transit.domain.repository.TransitRouteRepository;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import com.example.mohago_nocar.transit.infrastructure.messaging.consumer.ApiRequestEventConsumer;
import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiUriGenerator;
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

    private final ODsayApiUriGenerator uriGenerator;
    private final FestivalRepository festivalRepository;
    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final OdsayApiRequestEntryRepository apiRequestEntryRepository;
    private final TransitRouteRepository transitRouteRepository;
    private final RouteSegmentRepository routeSegmentRepository;

    // 새로운 축제가 생성되면 호출
    @Transactional
    public void saveTransitRoute(Long festivalId) {
        Festival festival = festivalRepository.getFestivalById(festivalId);
        List<FestivalNearPlace> nearPlaces = festivalNearPlaceRepository.findByFestivalId(festival.getId());
        List<Location> travelLocations = extractLocationsIn(festival, nearPlaces);

        List<OdsayApiRequestEntry> requests = new ArrayList<>();

        for (Location from : travelLocations) {
            for (Location to : travelLocations) {
                if (to.equals(from)) {
                    continue;
                }
                requests.add(createApiCallRequest(from, to));
            }
        }

        apiRequestEntryRepository.saveAll(requests);
    }

    private List<Location> extractLocationsIn(Festival festival, List<FestivalNearPlace> travelPlaces) {
        return Stream.concat(
                Stream.of(festival.getLocation()),
                travelPlaces.stream().map(FestivalNearPlace::getLocation)
        ).toList();
    }

    private OdsayApiRequestEntry createApiCallRequest(Location from, Location to) {
        URI requestURI = uriGenerator.buildRequestURI(
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude()
        );

        return OdsayApiRequestEntry.of(requestURI, RoutePoint.parse(from), RoutePoint.parse(to));
    }

    @Transactional(readOnly = true)
    @Override
    public TransitRouteWithSegments findTransitRouteWithSegments(Location from, Location to) {
        TransitRoute transitRoute = transitRouteRepository.findByDepartureAndArrival(
                        RoutePoint.parse(from),
                        RoutePoint.parse(to));

        List<RouteSegment> segment = routeSegmentRepository.findByTransitRouteId(transitRoute.getId());

        return TransitRouteWithSegments.from(transitRoute, segment);
    }

    @Override
    public TransitRoute findTransitRoute(Location from, Location to) {
        return transitRouteRepository.findByDepartureAndArrival(
                RoutePoint.parse(from),
                RoutePoint.parse(to));
    }

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
