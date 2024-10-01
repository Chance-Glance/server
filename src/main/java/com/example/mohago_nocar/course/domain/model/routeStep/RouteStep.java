package com.example.mohago_nocar.course.domain.model.routeStep;

import com.example.mohago_nocar.course.domain.model.vo.Location;
import com.example.mohago_nocar.global.common.domain.BaseEntity;
import com.example.mohago_nocar.global.util.DurationToIntervalConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RouteStep extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private Long courseId;

    @NotNull
    private Integer distance;

    @NotNull
    private Integer stepOrder;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "start_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "start_latitude"))
    })
    private Location startLocation;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "end_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "end_latitude"))
    })
    private Location endLocation;

    @NotNull
    @Convert(converter = DurationToIntervalConverter.class)
    private Duration timeTaken;

    public static RouteStep from(Long courseId, Integer distance, Integer stepOrder,
                                 Location startLocation, Location endLocation, Duration timeTaken
    ) {
        return RouteStep.builder()
                .courseId(courseId)
                .distance(distance)
                .stepOrder(stepOrder)
                .startLocation(startLocation)
                .endLocation(endLocation)
                .timeTaken(timeTaken)
                .build();
    }

    @Builder
    private RouteStep(Long courseId, Integer distance, Integer stepOrder,
                     Location startLocation, Location endLocation, Duration timeTaken
    ) {
        this.courseId = courseId;
        this.distance = distance;
        this.stepOrder = stepOrder;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.timeTaken = timeTaken;
    }
}
