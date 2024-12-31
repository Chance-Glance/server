package com.example.mohago_nocar.transit.domain.model;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

// TODO : TransitRoute

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class TransitRoute extends BaseEntity {

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

    private int totalTime;
    private double totalDistance;

    public static TransitRoute from(RoutePoint departure, RoutePoint arrival, int totalTime, double totalDistance) {
        return TransitRoute.builder()
                .departure(departure)
                .arrival(arrival)
                .totalTime(totalTime)
                .totalDistance(totalDistance)
                .build();
    }

    @Builder
    private TransitRoute(RoutePoint departure, RoutePoint arrival, int totalTime, double totalDistance) {
        this.departure = departure;
        this.arrival = arrival;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
    }

}
