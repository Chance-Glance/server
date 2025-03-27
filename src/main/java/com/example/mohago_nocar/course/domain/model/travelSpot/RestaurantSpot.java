package com.example.mohago_nocar.course.domain.model.travelSpot;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("RESTAURANT")
public class RestaurantSpot extends TravelSpot {

    public static RestaurantSpot from(Long courseId, Integer order) {
        return RestaurantSpot.builder()
                .courseId(courseId)
                .order(order)
                .build();
    }

    @Builder
    private RestaurantSpot(Long courseId, Integer order) {
        this.courseId = courseId;
        this.spotOrder = order;
    }
}
