package com.example.mohago_nocar.course.domain.model.travelSpot;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("ATTRACTION")
public class AttractionSpot extends TravelSpot {

    public static AttractionSpot from(Long courseId, Integer order) {
        return AttractionSpot.builder()
                .courseId(courseId)
                .order(order)
                .build();
    }

    @Builder
    private AttractionSpot(Long courseId, Integer order) {
        this.courseId = courseId;
        this.spotOrder = order;
    }
}
