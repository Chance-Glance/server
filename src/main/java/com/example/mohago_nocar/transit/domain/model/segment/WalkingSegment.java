package com.example.mohago_nocar.transit.domain.model.segment;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TrafficType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class WalkingSegment implements RouteSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;
    private int sectionTime;
    private int sequence;
    private Long transitRouteId;

    public static WalkingSegment from(
            double distance,
            int sectionTime,
            int sequence
    ) {
        return WalkingSegment.builder()
                .distance(distance)
                .sectionTime(sectionTime)
                .sequence(sequence)
                .build();
    }

    public static WalkingSegment from(
            double distance,
            int sectionTime,
            int sequence,
            Long transitRouteId
    ) {
        return WalkingSegment.builder()
                .distance(distance)
                .sectionTime(sectionTime)
                .sequence(sequence)
                .transitRouteId(transitRouteId)
                .build();
    }

    @Builder
    private WalkingSegment(
            double distance,
            int sectionTime,
            Long transitRouteId,
            int sequence
    ) {
        this.transitRouteId = transitRouteId;
        this.distance = distance;
        this.sectionTime = sectionTime;
        this.sequence = sequence;
    }

    @Override
    public TrafficType getTrafficType() {
        return TrafficType.WALK;
    }

    // todo: 도보는 출발 및 도착 지점 정보 제공하지 않음.
    @Override
    public RoutePoint getDeparture() {
        return null;
    }

    @Override
    public RoutePoint getArrival() {
        return null;
    }

    @Override
    public void setTransitId(Long transitId) {
        this.transitRouteId = transitId;
    }

}
