package com.example.mohago_nocar.course.domain.model.travelSpot;

import com.example.mohago_nocar.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "spot_type")
public abstract class TravelSpot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;

    @NotNull
    protected Long courseId;

    @NotNull
    protected Integer spotOrder;
}
