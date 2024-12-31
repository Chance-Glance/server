package com.example.mohago_nocar.transit.infrastructure.messaging.persistence;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.URI;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OdsayApiRequestEvent extends BaseEntity {

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

    @NotNull
    private URI requestUri;

    private Integer retryCount;


    public static OdsayApiRequestEvent of(URI requestUri, RoutePoint departure, RoutePoint arrival) {
        return OdsayApiRequestEvent.builder()
                .requestUri(requestUri)
                .departure(departure)
                .arrival(arrival)
                .retryCount(0)
                .build();
    }

    public static OdsayApiRequestEvent parse(OdsayApiRequestEntry item) {
        return OdsayApiRequestEvent.of(item.getRequestUri(), item.getDeparture(), item.getArrival());
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public boolean isMaxRetry() {
        return retryCount >= 3;
    }

    @Builder
    private OdsayApiRequestEvent(URI requestUri, RoutePoint departure, RoutePoint arrival, Integer retryCount) {
        this.requestUri = requestUri;
        this.departure = departure;
        this.arrival = arrival;
        this.retryCount = retryCount;
    }

}
