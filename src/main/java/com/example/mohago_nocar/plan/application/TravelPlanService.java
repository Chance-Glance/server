package com.example.mohago_nocar.plan.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.global.common.exception.InvalidValueException;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.plan.domain.service.TravelPlanUseCase;
import com.example.mohago_nocar.plan.presentation.request.PlanTravelCourseRequestDto;
import com.example.mohago_nocar.plan.presentation.response.PlanTravelCourseResponseDto;
import com.example.mohago_nocar.transit.domain.model.TransitInfo;
import com.example.mohago_nocar.transit.domain.model.WalkPath;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayDistanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.mohago_nocar.plan.presentation.exception.PlanErrorCode.TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelPlanService implements TravelPlanUseCase {

    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final FestivalRepository festivalRepository;
    private final TransitUseCase transitUseCase;

    private static final int EARTH_RADIUS = 6371;

    private int calcTravelTime(List<Location> route,  Map<Location, Map<Location, TransitInfo>> transitMaps) {
        int n = route.size();

        int travelTime = 0;
        for (int i = 0; i < n - 1; i++) {
            travelTime += transitMaps.get(route.get(i)).get(route.get(i + 1)).getTotalTime();
        }
        return travelTime;
    }

    private void routeBacktracking(int k, List<Location> locations, Map<Location, Map<Location, TransitInfo>> transitMaps,
                                   List<Location> optimal, List<Location> route, List<Boolean> isSelected) {
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

    private Double convertLongitudeToKmDist(Double dx, Double stdLatitude) {

        return EARTH_RADIUS * dx * Math.cos(stdLatitude) * Math.PI / 180;
    }

    private Double convertLatitudeToKmDist(Double dy) {

        return EARTH_RADIUS * dy * Math.PI / 180;
    }

    private List<Location> getOptimalRoute(List<Location> allLocations) {
        int locationCount = allLocations.size();
        Map<Location, Map<Location, TransitInfo>> fromToTransitInfoMap = new HashMap<>();

        for (int fromIndex = 0; fromIndex < locationCount; fromIndex++) {
            Map<Location, TransitInfo> toLocationTransitInfoMap = new HashMap<>();

            for (int toIndex = 0; toIndex < locationCount; toIndex++) {

                if (fromIndex == toIndex) {
                    continue;
                }

                Location fromLocation = allLocations.get(fromIndex);
                Location toLocation = allLocations.get(toIndex);

                TransitInfo transitInfo = getTransitInfoBetweenLocations(fromLocation, toLocation);
                toLocationTransitInfoMap.put(toLocation, transitInfo);
            }

            fromToTransitInfoMap.put(allLocations.get(fromIndex), toLocationTransitInfoMap);
        }

        List<Location> route = new ArrayList<>();
        List<Boolean> isSelected = new ArrayList<>();
        List<Location> optimalRoute = new ArrayList<>();
        for (int i = 0; i < locationCount; i++) {
            isSelected.add(false);
            optimalRoute.add(allLocations.get(i));
        }

        routeBacktracking(0, allLocations, fromToTransitInfoMap , optimalRoute, route, isSelected);
        return optimalRoute;
    }

    @Override
    public List<PlanTravelCourseResponseDto> planCourse(PlanTravelCourseRequestDto dto) {
        Festival festival = validateAndGetFestival(dto);
        List<FestivalNearPlace> travelPlaces = getTravelPlaces(dto.travelPlaceIds());

        List<Location> allLocations = combineLocations(festival, travelPlaces);
        List<Location> optimizedRoute = getOptimalRoute(allLocations);

        Map<Location, String> locationNameInfo = createLocationNameMap(festival, optimizedRoute);

        return createTravelCourse(optimizedRoute, locationNameInfo);
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

    private List<FestivalNearPlace> getTravelPlaces(List<Long> placeIds) {
        return placeIds.stream()
                .map(festivalNearPlaceRepository::findById)
                .toList();
    }

    private List<Location> combineLocations(Festival festival, List<FestivalNearPlace> travelPlaces) {
        return Stream.concat(
                Stream.of(festival.getLocation()),
                travelPlaces.stream().map(FestivalNearPlace::getLocation)
        ).toList();
    }

    private Map<Location, String> createLocationNameMap(Festival festival, List<Location> locations) {
        return locations.stream()
                .collect(Collectors.toMap(
                        location -> location,
                        location -> getPlaceName(festival, location)
                ));
    }

    private String getPlaceName(Festival festival, Location location) {
        if (location.equals(festival.getLocation())) {
            return festival.getName();
        }

        return festivalNearPlaceRepository.getPlaceNameByLocation(location);
    }

    private List<PlanTravelCourseResponseDto> createTravelCourse(List<Location> optimizedRoute, Map<Location, String> locationNameInfo) {
        List<PlanTravelCourseResponseDto> travelCourse = new ArrayList<>();

        for (int i = 0; i < optimizedRoute.size() - 1; i++) {
            Location fromLocation = optimizedRoute.get(i);
            Location toLocation = optimizedRoute.get(i + 1);

            String fromName = locationNameInfo.get(fromLocation);
            String toName = locationNameInfo.get(toLocation);

            TransitInfo transitInfo = getTransitInfoBetweenLocations(fromLocation, toLocation);

            travelCourse.add(createResponseDto(fromLocation, fromName, toLocation, toName, transitInfo));
        }

        return travelCourse;
    }

    private TransitInfo getTransitInfoBetweenLocations(Location fromLocation, Location toLocation) {
        try {
            return transitUseCase.findRouteTransitBetweenPlaces(fromLocation, toLocation);

        } catch (OdsayDistanceException e) {
            double dist = getKmDist(fromLocation, toLocation);
            int totalTime = (int) Math.round(dist * 15);
            WalkPath walkPath = new WalkPath(dist, totalTime);

            return TransitInfo.from(totalTime, dist, List.of(walkPath));
        }
    }

    /**
     * 두 위치(Location) 사이의 거리를 킬로미터 단위로 계산합니다.
     * @param startLocation
     * @param endLocation
     * @return 두 위치(Location) 사이의 거리
     */
    private Double getKmDist(Location startLocation, Location endLocation) {
        Double dx = Math.abs(startLocation.getLongitude() - endLocation.getLongitude());
        dx = Math.min(dx, 360 - dx);

        Double dy = Math.abs(startLocation.getLatitude() - endLocation.getLatitude());

        Double longitudeDist = convertLongitudeToKmDist(dx, startLocation.getLatitude());
        Double latitudeDist = convertLatitudeToKmDist(dy);

        return Math.sqrt(longitudeDist * longitudeDist + latitudeDist * latitudeDist);
    }

    private PlanTravelCourseResponseDto createResponseDto(Location fromLocation, String fromName, Location toLocation, String toName, TransitInfo transitInfo) {
        return PlanTravelCourseResponseDto.of(fromLocation, fromName, toLocation, toName, transitInfo);
    }
}
