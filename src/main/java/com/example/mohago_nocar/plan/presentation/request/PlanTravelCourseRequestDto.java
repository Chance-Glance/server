package com.example.mohago_nocar.plan.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PlanTravelCourseRequestDto(
        Long festivalId,
        LocalDate travelDate,

        @Schema(description = "출발 시간", example = "09:10")
        LocalTime leaveTime,

        @Schema(description = "도착 시간", example = "10:00")
        LocalTime arrivalTime,

        @Schema(description = "googlePlaceId 리스트", example = "[\"ChIJZ4iA7KrebjURMcTOW3a5dJ0\"]")
        List<String> travelPlaceIds
) {
}
