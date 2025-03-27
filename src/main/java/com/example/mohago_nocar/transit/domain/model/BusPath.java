package com.example.mohago_nocar.transit.domain.model;

import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.PathType.BUS;

@Getter
public class BusPath extends SubPath{

    private final String busNo; // 버스 번호
    private final int busType; // 버스 타입
    private final String startName; // 출발 지점 이름
    private final double startX; // 출발 지점 X 좌표
    private final double startY; // 출발 지점 Y 좌표
    private final String endName; // 도착 지점 이름
    private final double endX; // 도착 지점 X 좌표
    private final double endY; // 도착 지점 Y 좌표

    public BusPath(
            double distance,
            int sectionTime,
            String busNo,
            int busType,
            String startName,
            double startX,
            double startY,
            String endName,
            double endX,
            double endY
    ) {
        super(distance, sectionTime);
        this.busNo = busNo;
        this.busType = busType;
        this.startName = startName;
        this.startX = startX;
        this.startY = startY;
        this.endName = endName;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public PathType getPathType() {
        return BUS;
    }

    @Override
    public String toString() {
        return "BusPath{" +
                "busNo='" + busNo + '\'' +
                ", busType=" + busType +
                ", startName='" + startName + '\'' +
                ", startX=" + startX +
                ", startY=" + startY +
                ", endName='" + endName + '\'' +
                ", endX=" + endX +
                ", endY=" + endY +
                ", distance=" + distance +
                ", sectionTime=" + sectionTime +
                '}';
    }
}
