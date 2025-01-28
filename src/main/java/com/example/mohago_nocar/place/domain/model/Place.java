package com.example.mohago_nocar.place.domain.model;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
public class Place {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Location location;

    @NotNull
    private String address;

    @NotNull
    private String placeUrl;

    @NotNull
    @Enumerated(value = STRING)
    private PlaceCategory category;

    public static Place from(
            String id,
            String name,
            Location location,
            String address,
            String placeUrl,
            PlaceCategory category
    ) {
        return Place.builder()
                .id(id)
                .name(name)
                .location(location)
                .address(address)
                .placeUrl(placeUrl)
                .category(category)
                .build();
    }

    @Builder
    private Place(
            String id,
            String name,
            Location location,
            String address,
            String placeUrl,
            PlaceCategory category
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.placeUrl = placeUrl;
        this.category = category;
    }

}
