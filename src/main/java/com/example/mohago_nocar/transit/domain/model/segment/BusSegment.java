package com.example.mohago_nocar.transit.domain.model.segment;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TrafficType;
import jakarta.persistence.*;
import lombok.*;

import static com.example.mohago_nocar.transit.domain.model.TrafficType.BUS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BusSegment implements RouteSegment {

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

    private int sequence;
    private Long transitRouteId;
    private double distance;
    private int sectionTime;
    private String busNo;
    private int busType;

    public static BusSegment from(
            double distance,
            int sectionTime,
            String busNo,
            int busType,
            RoutePoint departure,
            RoutePoint arrival,
            int sequence
    ) {
        return BusSegment.builder()
                .distance(distance)
                .sectionTime(sectionTime)
                .busNo(busNo)
                .busType(busType)
                .departure(departure)
                .arrival(arrival)
                .sequence(sequence)
                .build();
    }

    @Override
    public TrafficType getTrafficType() {
        return BUS;
    }

    @Override
    public void setTransitId(Long transitId) {
        this.transitRouteId = transitId;
    }

    @Builder
    private BusSegment(
            double distance,
            int sectionTime,
            String busNo,
            int busType,
            RoutePoint departure,
            RoutePoint arrival,
            int sequence,
            Long transitRouteId
    ) {
        this.distance = distance;
        this.sectionTime = sectionTime;
        this.busNo = busNo;
        this.busType = busType;
        this.departure = departure;
        this.arrival = arrival;
        this.sequence = sequence;
        this.transitRouteId = transitRouteId;
    }

}
