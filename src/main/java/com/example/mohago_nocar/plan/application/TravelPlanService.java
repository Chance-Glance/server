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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.mohago_nocar.plan.presentation.exception.PlanErrorCode.TRAVEL_DATE_NOT_IN_FESTIVAL_PERIOD;

@Service
@RequiredArgsConstructor
public class TravelPlanService implements TravelPlanUseCase {

    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final FestivalRepository festivalRepository;

    @Override
    public PlanTravelCourseResponseDto planCourse(PlanTravelCourseRequestDto dto) {
        Festival festival = getFestivalAndValidateTravelDate(dto);
        Location festivalLocation = festival.getLocation();

        List<FestivalNearPlace> travelPlaces = getTravelPlaces(dto.travelPlaceIds());

        // TODO: 여행 코스 추천 알고리즘 구현

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
