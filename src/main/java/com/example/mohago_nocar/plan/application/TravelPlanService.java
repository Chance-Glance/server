package com.example.mohago_nocar.plan.application;

import com.example.mohago_nocar.course.domain.service.CourseUseCase;
import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.global.common.exception.InvalidValueException;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.plan.domain.service.TravelPlanUseCase;
import com.example.mohago_nocar.plan.presentation.request.PlanTravelCourseRequestDto;
import com.example.mohago_nocar.plan.presentation.response.TransitRouteResponseDto;
import com.example.mohago_nocar.transit.domain.model.TransitRouteWithSegments;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.example.mohago_nocar.plan.presentation.exception.PlanErrorCode.TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelPlanService implements TravelPlanUseCase {

    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final FestivalRepository festivalRepository;
    private final TransitUseCase transitUseCase;
    private final CourseUseCase courseUseCase;

    @Override
    public List<TransitRouteResponseDto> planCourse(PlanTravelCourseRequestDto dto) {
        Festival festival = validateAndGetFestival(dto);
        List<FestivalNearPlace> nearPlaces = getFestivalNearPlaces(dto.travelPlaceIds());
        List<Location> toBeVisitLocations = combineLocations(festival, nearPlaces);

        List<Location> optimizedRoute = courseUseCase.getOptimalCourse(toBeVisitLocations);
        List<TransitRouteWithSegments> travelCourse = getOptimalTravelCourse(optimizedRoute);

        return travelCourse.stream()
                .map(route -> TransitRouteResponseDto.of(
                        festival.getName(), festival.getLocation(), route))
                .toList();
    }

    private List<TransitRouteWithSegments> getOptimalTravelCourse(List<Location> optimizedRoute) {
        List<TransitRouteWithSegments> travelCourse = new ArrayList<>();

        for (int i = 0; i < optimizedRoute.size() - 1; i++) {
            int fromIndex = i;
            int toIndex = i + 1;

            Location fromLocation = optimizedRoute.get(fromIndex);
            Location toLocation = optimizedRoute.get(toIndex);

            TransitRouteWithSegments route = findTransitRouteWithSegments(fromLocation, toLocation);
            travelCourse.add(route);
        }

        return travelCourse;
    }

    private TransitRouteWithSegments findTransitRouteWithSegments(Location fromLocation, Location toLocation) {
        return transitUseCase.findTransitRouteWithSegments(fromLocation, toLocation);
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

    private List<FestivalNearPlace> getFestivalNearPlaces(List<Long> placeIds) {
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

}
