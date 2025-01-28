package com.example.mohago_nocar.plan.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.global.common.exception.InternalServerException;
import com.example.mohago_nocar.global.common.exception.InvalidValueException;
import com.example.mohago_nocar.place.application.PlaceService;
import com.example.mohago_nocar.place.domain.model.Place;
import com.example.mohago_nocar.place.domain.repository.PlaceRepository;
import com.example.mohago_nocar.plan.domain.model.TravelLocationWithName;
import com.example.mohago_nocar.plan.domain.model.TravelCatalog;
import com.example.mohago_nocar.plan.domain.service.TravelPlanUseCase;
import com.example.mohago_nocar.plan.presentation.request.PlanTravelCourseRequestDto;
import com.example.mohago_nocar.plan.presentation.response.PlanTravelCourseResponseDto;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;
import com.example.mohago_nocar.transit.domain.model.WalkPath;
import com.example.mohago_nocar.transit.infrastructure.externalApi.google.GoogleApiClient;
import com.example.mohago_nocar.transit.infrastructure.externalApi.google.dto.response.GoogleDistanceMatrixResponse;
import com.example.mohago_nocar.transit.infrastructure.externalApi.converter.DistanceMatrixConverter;
import com.example.mohago_nocar.transit.domain.converter.TransitRouteConverter;
import com.example.mohago_nocar.transit.infrastructure.externalApi.google.dto.response.RouteSpecification;
import com.example.mohago_nocar.transit.infrastructure.externalApi.odsay.ODsayApiClient;
import com.example.mohago_nocar.transit.infrastructure.externalApi.odsay.dto.response.OdsayRouteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static com.example.mohago_nocar.plan.presentation.exception.PlanErrorCode.TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelPlanService implements TravelPlanUseCase {

    private static final int FIRST = 0;

    private final PlaceRepository placeRepository;
    private final FestivalRepository festivalRepository;
    private final PlaceService placeService;
    private final GoogleApiClient googleApiClient;
    private final ODsayApiClient oDsayApiClient;

    @Override
    public List<PlanTravelCourseResponseDto> planCourse(PlanTravelCourseRequestDto dto) {
        Festival festival = validateAndGetFestival(dto);
        List<Place> places = getTravelPlaces(festival, dto.placeIds());

        TravelCatalog travelCatalog = TravelCatalog.of(festival, places);
        List<Location> distinctLocations = travelCatalog.getDistinctLocations();

        List<RouteSpecification> routeSpecifications = fetchDistanceAndDurationBetween(distinctLocations);
        List<Location> optimizedRoute = getOptimalRoute(distinctLocations, routeSpecifications);

        List<PlanTravelCourseResponseDto> travelCourse = createTravelCourse(optimizedRoute, travelCatalog);
        log.info(travelCourse.toString());
        return travelCourse;
    }

    private Festival validateAndGetFestival(PlanTravelCourseRequestDto dto) {
        Festival festival = festivalRepository.getFestivalById(dto.festivalId());
        ensureTravelDateDuringFestival(festival, dto.travelDate());
        return festival;
    }

    private void ensureTravelDateDuringFestival(Festival festival, LocalDate travelDate) {
        if (!festival.isDateDuringFestival(travelDate)) {
            throw new InvalidValueException(TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD);
        }
    }

    private List<Place> getTravelPlaces(Festival festival, List<String> placeIds) {
        List<Place> places = placeRepository.findByIds(festival.getId(), placeIds);
        if (places.isEmpty()) {
            places = placeService.cachePlaces(festival.getId(), festival.getLocation()).stream()
                    .filter(place -> placeIds.contains(place.getId()))
                    .toList();
        }
        return places;
    }

    private List<RouteSpecification> fetchDistanceAndDurationBetween(List<Location> distinctLocations) {
        List<RouteSpecification> routeSpecs = new ArrayList<>();
        int toVisitSize = distinctLocations.size();

        for (int originIndex = FIRST; originIndex < toVisitSize; originIndex++) {
            Location origin = distinctLocations.get(originIndex);
            List<Location> destinations = createDestination(distinctLocations, originIndex);

            GoogleDistanceMatrixResponse matrix = googleApiClient.getDistanceMatrix(origin, destinations);
            routeSpecs.addAll(
                    DistanceMatrixConverter.convertMatrixToRouteSpecs(matrix, toVisitSize -1, origin, destinations));
        }

        return routeSpecs;
    }

    private List<Location> createDestination(List<Location> distinctLocations, int excludeIndex) {
        return IntStream.range(0, distinctLocations.size())
                .filter(index -> index != excludeIndex)
                .mapToObj(distinctLocations::get)
                .toList();
    }

    private List<Location> getOptimalRoute(List<Location> distinctLocations, List<RouteSpecification> routeSpecificationBetweenLocations) {
        int locationCount = distinctLocations.size();
        Map<Location, Map<Location, RouteSpecification>> fromToTransitInfoMap = new HashMap<>();

        for (int originIndex = FIRST; originIndex < locationCount; originIndex++) {
            Map<Location, RouteSpecification> toLocationTransitInfoMap = new HashMap<>();

            for (int destinationIndex = FIRST; destinationIndex < locationCount; destinationIndex++) {

                if (originIndex == destinationIndex) {
                    continue;
                }

                Location origin = distinctLocations.get(originIndex);
                Location destination = distinctLocations.get(destinationIndex);

                RouteSpecification routeSpec = getMatchedRouteSpec(origin, destination, routeSpecificationBetweenLocations);
                toLocationTransitInfoMap.put(destination, routeSpec);
            }

            fromToTransitInfoMap.put(distinctLocations.get(originIndex), toLocationTransitInfoMap);
        }

        List<Location> route = new ArrayList<>();
        List<Boolean> isSelected = new ArrayList<>();
        List<Location> optimalRoute = new ArrayList<>();
        for (int i = 0; i < locationCount; i++) {
            isSelected.add(false);
            optimalRoute.add(distinctLocations.get(i));
        }

        routeBacktracking(0, distinctLocations, fromToTransitInfoMap , optimalRoute, route, isSelected);
        return optimalRoute;
    }

    private RouteSpecification getMatchedRouteSpec(
            Location origin,
            Location destination,
            List<RouteSpecification> routeSpecificationBetweenLocations
    ) {
        Optional<RouteSpecification> routeSpecification = routeSpecificationBetweenLocations.stream()
                .filter(route -> route.isEqualLocation(origin, destination))
                .findFirst();

        if (routeSpecification.isEmpty()) {
            log.error("origin-{}, destination-{}를 가지는 RouteSpec을 찾을 수 없습니다.", origin, destination);
            throw new InternalServerException();
        }

        return routeSpecification.get();
    }

    private void routeBacktracking(
            int k,
            List<Location> locations,
            Map<Location, Map<Location, RouteSpecification>> transitMaps,
            List<Location> optimal,
            List<Location> route,
            List<Boolean> isSelected
    ) {
        int n = locations.size();

        if (k == n)
        {
            int t1 = calcTravelTime(optimal, transitMaps);
            int t2 = calcTravelTime(route, transitMaps);

            if (t1 > t2) {
                for (int i = 0; i < n; i++) {
                    optimal.set(i, route.get(i));
                }
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (isSelected.get(i)) {
                continue;
            }

            isSelected.set(i, true);
            route.add(locations.get(i));
            routeBacktracking(k + 1, locations, transitMaps, optimal, route, isSelected);
            route.removeLast();
            isSelected.set(i, false);
        }
    }

    private int calcTravelTime(List<Location> route,  Map<Location, Map<Location, RouteSpecification>> routeMaps) {
        int n = route.size();

        int travelTime = 0;
        for (int i = 0; i < n - 1; i++) {
            travelTime += routeMaps.get(route.get(i)).get(route.get(i + 1)).durationInMinutes();
        }
        return travelTime;
    }


    private List<PlanTravelCourseResponseDto> createTravelCourse(
            List<Location> optimizedRoute,
            TravelCatalog travelCatalog
    ) {
        Map<Location, String> locationNameMap = travelCatalog.createLocationNameMap();
        List<PlanTravelCourseResponseDto> travelCourse = new ArrayList<>();

        for (int index = FIRST; index < optimizedRoute.size() - 1; index++) {
            Location origin = optimizedRoute.get(index);
            Location destination = optimizedRoute.get(index + 1);

            String originName = handleDuplicateOrigins(origin, travelCourse, locationNameMap, travelCatalog);
            String destinationName = locationNameMap.get(destination);

            TransitRoute transitRoute = getTransitRouteBetweenLocations(origin, destination);
            travelCourse.add(createResponse(origin, originName, destination, destinationName, transitRoute));

            handleDuplicateDestinations(travelCatalog, travelCourse, destination, destinationName);
        }

        return travelCourse;
    }

    private String handleDuplicateOrigins(
            Location location,
            List<PlanTravelCourseResponseDto> travelCourse,
            Map<Location, String> locationNameMap,
            TravelCatalog travelCatalog
    ) {
        if (travelCatalog.checkDuplicateLocation(location)) {
            List<TravelLocationWithName> duplicatedLocations = travelCatalog.getTravelLocations(location);
            List<PlanTravelCourseResponseDto> responses = createResponses(duplicatedLocations);
            travelCourse.addAll(responses);
            return responses.getLast().endPlaceName(); // 마지막 장소 이름 반환
        }

        return locationNameMap.get(location); // 중복이 없으면 기존 이름 반환
    }

    private TransitRoute getTransitRouteBetweenLocations(Location origin, Location destination) {
        OdsayRouteResponse response = oDsayApiClient.searchTransitRoute(
                origin.getLongitude(), origin.getLatitude(), destination.getLongitude(), destination.getLatitude());
        return TransitRouteConverter.convertRouteResponseDtoToTransitRoute(response, origin, destination);
    }

    private void handleDuplicateDestinations(
            TravelCatalog travelCatalog, List<PlanTravelCourseResponseDto> travelCourse,
            Location destinationLocation, String destinationName
    ) {
        if (travelCatalog.checkDuplicateLocation(destinationLocation)) {
            List<TravelLocationWithName> duplicatedLocations = travelCatalog.getTravelLocations(destinationLocation);
            List<PlanTravelCourseResponseDto> responseDtos = createResponsesStartWith(destinationName, duplicatedLocations);
            travelCourse.addAll(responseDtos);
        }
    }

    private List<PlanTravelCourseResponseDto> createResponsesStartWith(
            String startName, List<TravelLocationWithName> duplicatedLocations) {
        // 시작 지점 탐색
        int startIndex = -1;
        for (int i = 0; i < duplicatedLocations.size(); i++) {
            if (duplicatedLocations.get(i).getName().equals(startName)) {
                startIndex = i;
                break;
            }
        }

        // 시작 지점이 없을 경우 예외 처리
        if (startIndex == -1) {
            throw new IllegalArgumentException("Start name not found in locations.");
        }

        if (startIndex != 0) {
            Collections.swap(duplicatedLocations, 0, startIndex);
        }

        return createResponses(duplicatedLocations);
    }

    private List<PlanTravelCourseResponseDto> createResponses(List<TravelLocationWithName> duplicatedLocations) {
        List<PlanTravelCourseResponseDto> responseBetweenSameLocations = new ArrayList<>();

        for (int from = 0; from < duplicatedLocations.size() -1; from++) {
            PlanTravelCourseResponseDto dto = PlanTravelCourseResponseDto.of(
                    duplicatedLocations.get(from).getLocation(),
                    duplicatedLocations.get(from).getName(),
                    duplicatedLocations.get(from + 1).getLocation(),
                    duplicatedLocations.get(from + 1).getName(),
                    TransitRoute.from(0, 0, List.of(new WalkPath((double) 0, 1)))
            );
            responseBetweenSameLocations.add(dto);
        }

        return responseBetweenSameLocations;
    }

    private PlanTravelCourseResponseDto createResponse(
            Location originLocation, String originName,
            Location destinationLocation, String destinationName,
            TransitRoute transitRoute
    ) {
        return PlanTravelCourseResponseDto.of(
                originLocation, originName, destinationLocation, destinationName, transitRoute);
    }

}
