package com.example.mohago_nocar.transit.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TransitRoute {

    private final int totalTime;
    private final double totalDistance;
    private final List<SubPath> subPaths;

    public static TransitRoute from(int totalTime, double totalDistance, List<SubPath> subPaths) {
        return TransitRoute.builder()
                .totalTime(totalTime)
                .totalDistance(totalDistance)
                .subPaths(subPaths)
                .build();
    }

    @Builder
    private TransitRoute(int totalTime, double totalDistance, List<SubPath> subPaths) {
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.subPaths = subPaths;
    }

    @Override
    public String toString() {
        List<String> collect = subPaths.stream().map(Object::toString).toList();

        return "TransitInfo{" +
                "totalDistance=" + totalDistance +
                ", subPaths=" + collect +
                '}';
    }
}
