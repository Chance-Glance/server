package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.TrafficType;
import lombok.Getter;

@Getter
public abstract class SubPathResponseDto {

    protected final double distance;
    protected final int sectionTime;
    protected final TrafficType trafficType;

    protected SubPathResponseDto(double distance, int sectionTime, TrafficType trafficType) {
        this.distance = distance;
        this.sectionTime = sectionTime;
        this.trafficType = trafficType;
    }

}
