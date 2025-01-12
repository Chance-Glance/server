package com.example.mohago_nocar.festival.domain.model;

import com.example.mohago_nocar.festival.domain.model.vo.ActivePeriod;
import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Festival extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    private ActivePeriod activePeriod;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private String address;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "placeName", column = @Column(name = "name")),
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Location location;

    public static Festival from(ActivePeriod activePeriod, String description, String address, Location location) {
        return Festival.builder()
                .activePeriod(activePeriod)
                .description(description)
                .address(address)
                .location(location)
                .build();
    }

    @Builder
    private Festival( ActivePeriod activePeriod, String description, String address, Location location) {
        this.activePeriod = activePeriod;
        this.description = description;
        this.address = address;
        this.location = location;
    }


    public boolean isDateDuringFestival(LocalDate travelDate) {
        return activePeriod.containsDate(travelDate);
    }
}
