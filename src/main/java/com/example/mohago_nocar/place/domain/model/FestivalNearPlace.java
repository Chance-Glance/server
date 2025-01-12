package com.example.mohago_nocar.place.domain.model;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FestivalNearPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private Long festivalId;

    @NotNull
    @Embedded
    private OperatingSchedule operatingSchedule;

    @NotNull
    @Embedded
    private Location location;

    @NotNull
    private String address;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(value = STRING)
    private PlaceType placeType;

    @NotNull
    private String googlePlaceId;

    public static FestivalNearPlace from(
            Long festivalId,
            OperatingSchedule operatingSchedule,
            Location location,
            String address,
            String description,
            PlaceType placeType,
            String googlePlaceId
    ) {
        return FestivalNearPlace.builder()
                .festivalId(festivalId)
                .operatingSchedule(operatingSchedule)
                .location(location)
                .address(address)
                .description(description)
                .placeType(placeType)
                .googlePlaceId(googlePlaceId)
                .build();
    }

    @Builder
    private FestivalNearPlace(
            Long festivalId,
            OperatingSchedule operatingSchedule,
            Location location,
            String address,
            String description,
            PlaceType placeType,
            String googlePlaceId
    ) {
        this.festivalId = festivalId;
        this.operatingSchedule = operatingSchedule;
        this.location = location;
        this.address = address;
        this.description = description;
        this.placeType = placeType;
        this.googlePlaceId = googlePlaceId;
    }
}
