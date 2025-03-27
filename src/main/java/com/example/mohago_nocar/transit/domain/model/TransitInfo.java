package com.example.mohago_nocar.transit.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class TransitInfo {

    private final int totalTime;
    private final double totalDistance;
    private final List<SubPath> subPaths;

    public static TransitInfo from(int totalTime, double totalDistance, List<SubPath> subPaths) {
        return TransitInfo.builder()
                .totalTime(totalTime)
                .totalDistance(totalDistance)
                .subPaths(subPaths)
                .build();
    }

    @Builder
    private TransitInfo(int totalTime, double totalDistance, List<SubPath> subPaths) {
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
