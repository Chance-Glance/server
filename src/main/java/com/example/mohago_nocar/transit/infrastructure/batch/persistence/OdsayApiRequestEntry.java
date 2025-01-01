package com.example.mohago_nocar.transit.infrastructure.batch.persistence;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.URI;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OdsayApiRequestEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private URI requestUri;

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

    public static OdsayApiRequestEntry of(URI requestUri, RoutePoint departure, RoutePoint arrival) {
        return OdsayApiRequestEntry.builder()
                .requestUri(requestUri)
                .departure(departure)
                .arrival(arrival)
                .build();
    }

    @Builder
    private OdsayApiRequestEntry(URI requestUri, RoutePoint departure, RoutePoint arrival) {
        this.requestUri = requestUri;
        this.departure = departure;
        this.arrival = arrival;
    }

}
