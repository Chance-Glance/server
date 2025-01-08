package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.model.segment.SubwaySegment;
import lombok.Builder;
import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.TrafficType.SUBWAY;

@Getter
public class SubwayPathResponseDto extends SubPathResponseDto {

    private final String subwayLineName;
    private final LocationResponse departure;
    private final LocationResponse arrival;

    public static SubwayPathResponseDto of(RouteSegment routeSegment) {
        SubwaySegment segment = (SubwaySegment) routeSegment;

        return SubwayPathResponseDto.builder()
                .distance(segment.getDistance())
                .sectionTime(segment.getSectionTime())
                .subwayLineName(segment.getSubwayLineName())
                .departure(LocationResponse.of(segment.getDeparture().getName(), segment.getDeparture().getLongitude(), segment.getDeparture().getLatitude()))
                .arrival(LocationResponse.of(segment.getArrival().getName(), segment.getArrival().getLongitude(), segment.getArrival().getLatitude()))
                .build();
    }

    @Builder
    private SubwayPathResponseDto(
            double distance,
            int sectionTime,
            String subwayLineName,
            LocationResponse departure,
            LocationResponse arrival
    ) {
        super(distance, sectionTime, SUBWAY);
        this.subwayLineName = subwayLineName;
        this.departure = departure;
        this.arrival = arrival;
    }
}
