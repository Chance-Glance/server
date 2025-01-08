package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.segment.BusSegment;
import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import lombok.Builder;
import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.TrafficType.BUS;

@Getter
public class BusPathResponseDto extends SubPathResponseDto {

    private final String busNo;
    private final int busType;
    private final LocationResponse departure;
    private final LocationResponse arrival;

    public static BusPathResponseDto of(RouteSegment routeSegment) {
        BusSegment segment = (BusSegment) routeSegment;

        return BusPathResponseDto.builder()
                .distance(segment.getDistance())
                .sectionTime(segment.getSectionTime())
                .busNo(segment.getBusNo())
                .busType(segment.getBusType())
                .departure(LocationResponse.of(segment.getDeparture().getName(), segment.getDeparture().getLongitude(), segment.getDeparture().getLatitude()))
                .arrival(LocationResponse.of(segment.getArrival().getName(), segment.getArrival().getLongitude(), segment.getArrival().getLatitude()))
                .build();
    }

    @Builder
    private BusPathResponseDto(
            double distance,
            int sectionTime,
            String busNo,
            int busType,
            LocationResponse departure,
            LocationResponse arrival
    ) {
        super(distance, sectionTime, BUS);
        this.busNo = busNo;
        this.busType = busType;
        this.departure = departure;
        this.arrival = arrival;
    }
}
