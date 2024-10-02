package com.example.mohago_nocar.transit.domain.model;

import lombok.Getter;

@Getter
public abstract class SubPath {
    protected final double distance; // 구간 거리
    protected final int sectionTime; // 구간 소요 시간

    protected SubPath(double distance, int sectionTime) {
        this.distance = distance;
        this.sectionTime = sectionTime;
    }

    public abstract PathType getPathType();
}
