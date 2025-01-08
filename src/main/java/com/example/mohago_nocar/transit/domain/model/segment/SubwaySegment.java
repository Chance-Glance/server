package com.example.mohago_nocar.transit.domain.model.segment;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TrafficType;
import jakarta.persistence.*;
import lombok.*;

import static com.example.mohago_nocar.transit.domain.model.TrafficType.SUBWAY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SubwaySegment extends BaseEntity implements RouteSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "departure_name")),
            @AttributeOverride(name = "latitude", column = @Column(name = "departure_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "departure_longitude"))
    })
    private RoutePoint departure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "arrival_name")),
            @AttributeOverride(name = "latitude", column = @Column(name = "arrival_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "arrival_longitude"))
    })
    private RoutePoint arrival;

    private double distance;
    private int sectionTime;
    private String subwayLineName;
    private int sequence;
    private Long transitRouteId;

    public static SubwaySegment from(
            double distance,
            int sectionTime,
            String subwayLineName,
            RoutePoint departure,
            RoutePoint arrival,
            int sequence
    ) {
        return SubwaySegment.builder()
                .distance(distance)
                .sectionTime(sectionTime)
                .subwayLineName(subwayLineName)
                .departure(departure)
                .arrival(arrival)
                .sequence(sequence)
                .build();
    }

    @Override
    public TrafficType getTrafficType() {
        return SUBWAY;
    }

    @Override
    public void setTransitId(Long transitId) {
        this.transitRouteId = transitId;
    }

    @Builder
    private SubwaySegment(
            double distance,
            int sectionTime,
            String subwayLineName,
            RoutePoint departure,
            RoutePoint arrival,
            int sequence,
            Long transitRouteId
    ) {
        this.distance = distance;
        this.sectionTime = sectionTime;
        this.subwayLineName = subwayLineName;
        this.departure = departure;
        this.arrival = arrival;
        this.sequence = sequence;
        this.transitRouteId = transitRouteId;
    }

}
