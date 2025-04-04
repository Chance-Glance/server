package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.domain.model.TransitInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record PlanTravelCourseResponseDto(
        String startPlaceName,
        double startLongitude,
        double startLatitude,
        String endPlaceName,
        double endLongitude,
        double endLatitude,
        int totalTime,
        double totalDistance,
        List<SubPathResponseDto> subPaths
) {

    public static PlanTravelCourseResponseDto of(
            Location fromLocation,
            String fromName,
            Location toLocation,
            String toName,
            TransitInfo transitInfo
    ) {
        return PlanTravelCourseResponseDto.builder()
                .startPlaceName(fromName)
                .startLongitude(fromLocation.getLongitude())
                .startLatitude(fromLocation.getLatitude())
                .endPlaceName(toName)
                .endLongitude(toLocation.getLongitude())
                .endLatitude(toLocation.getLatitude())
                .totalTime(transitInfo.getTotalTime())
                .totalDistance(transitInfo.getTotalDistance())
                .subPaths(transitInfo.getSubPaths().stream()
                        .map(subPath ->
                            switch (subPath.getPathType()) {
                                case BUS -> BusPathResponseDto.of(subPath);
                                case WALK -> WalkPathResponseDto.of(subPath);
                                case SUBWAY -> SubwayPathResponseDto.of(subPath);
                            })
                        .toList()
                )
                .build();
    }
}
