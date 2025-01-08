package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.model.segment.WalkingSegment;
import lombok.Builder;
import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.TrafficType.WALK;

@Getter
public class WalkPathResponseDto extends SubPathResponseDto{

    public static WalkPathResponseDto of(RouteSegment routeSegment) {
        WalkingSegment segment = (WalkingSegment) routeSegment;

        return WalkPathResponseDto.builder()
                .distance(segment.getDistance())
                .sectionTime(segment.getSectionTime())
                .build();
    }

    @Builder
    private WalkPathResponseDto(double distance, int sectionTime) {
        super(distance, sectionTime, WALK);
    }
}
