package com.example.mohago_nocar.transit.domain.model;

import lombok.Getter;

@Getter
public class WalkPath extends SubPath{

    public WalkPath(double distance, int sectionTime) {
        super(distance, sectionTime);
    }

    @Override
    public PathType getPathType() {
        return PathType.WALK;
    }

    @Override
    public String toString() {
        return "WalkPath{" +
                "distance=" + distance +
                ", sectionTime=" + sectionTime +
                '}';
    }
}
