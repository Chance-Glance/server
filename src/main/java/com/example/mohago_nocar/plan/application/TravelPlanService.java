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
import com.example.mohago_nocar.transit.domain.model.SubPath;
import com.example.mohago_nocar.transit.domain.model.TransitInfo;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    int calcTravelTime(List<Location> route,  Map<Location, Map<Location, TransitInfo>> transitMaps) {
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

    private List<Location> getOptimalRoute(List<Location> locations) {
        Map<Location, Map<Location, TransitInfo>> transitMaps =
                locations.stream().collect(Collectors.toMap(
                        from -> from,
                        from -> locations.stream()
                                .filter(to -> !from.equals(to))
                                .collect(Collectors.toMap(
                                        to -> to,
                                        to -> transitUseCase.findRouteTransitBetweenPlaces(from, to)
                                ))
                ));

        int n = locations.size();

        List<Location> route = new ArrayList<>();
        List<Boolean> isSelected = new ArrayList<>();
        List<Location> optimalRoute = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            isSelected.add(false);
            optimalRoute.add(locations.get(i));
        }


        routeBacktracking(0, locations, transitMaps, optimalRoute, route, isSelected);
        return optimalRoute;
    }

    @Override
    public PlanTravelCourseResponseDto planCourse(PlanTravelCourseRequestDto dto) {
        Festival festival = getFestivalAndValidateTravelDate(dto);
        List<FestivalNearPlace> travelPlaces = getTravelPlaces(dto.travelPlaceIds());

        /*
        TransitInfo에는 아래와 같은 정보가 있음.
        private final int totalTime;
        private final double totalDistance;
        private final List<SubPath> subPaths;

         */

        // TODO: 여행 코스 추천 알고리즘 구현. 가야하는 순서 지켜서 list? 정도 만들어주세요. 응답은 송은이가..

        List<Location> locations = Stream.concat(
                Stream.of(festival.getLocation()),
                travelPlaces.stream().map(FestivalNearPlace::getLocation)
        ).toList();

        List<Location> travelRoute = getOptimalRoute(locations);
        for (Location location : travelRoute) {
            log.info(location.getLatitude().toString() + " " + location.getLongitude().toString());
        }

        return null;
    }

    private Festival getFestivalAndValidateTravelDate(PlanTravelCourseRequestDto dto) {
        Festival festival = festivalRepository.getFestivalById(dto.festivalId());
        validateTravelDateWithinFestivalPeriod(festival, dto.travelDate());
        return festival;
    }

    private void validateTravelDateWithinFestivalPeriod(Festival festival, LocalDate travelDate) {
        if (!festival.isDateWithinFestivalPeriod(travelDate)) {
            throw new InvalidValueException(TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD);
        }
    }

    private List<FestivalNearPlace> getTravelPlaces(List<String> googlePlaceIds) {
        return googlePlaceIds.stream()
                .map(festivalNearPlaceRepository::findByGooglePlaceId)
                .toList();
    }

}
