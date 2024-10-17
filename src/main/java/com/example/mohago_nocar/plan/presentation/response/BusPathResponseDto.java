package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.BusPath;
import com.example.mohago_nocar.transit.domain.model.SubPath;
import lombok.Builder;
import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.PathType.BUS;

@Getter
public class BusPathResponseDto extends SubPathResponseDto {

    private final String busNo;
    private final int busType;
    private final String startPlaceName;
    private final double startLongitude;
    private final double startLatitude;
    private final String endPlaceName;
    private final double endLongitude;
    private final double endLatitude;

    public static BusPathResponseDto of(SubPath subPath) {
        BusPath busPath = (BusPath) subPath;

        return BusPathResponseDto.builder()
                .distance(busPath.getDistance())
                .sectionTime(busPath.getSectionTime())
                .busNo(busPath.getBusNo())
                .busType(busPath.getBusType())
                .startPlaceName(busPath.getStartName())
                .startLongitude(busPath.getStartX())
                .startLatitude(busPath.getStartY())
                .endPlaceName(busPath.getEndName())
                .endLongitude(busPath.getEndX())
                .endLatitude(busPath.getEndY())
                .build();
    }

    @Builder
    private BusPathResponseDto(
            double distance,
            int sectionTime,
            String busNo,
            int busType,
            String startPlaceName,
            double startLongitude,
            double startLatitude,
            String endPlaceName,
            double endLongitude,
            double endLatitude
    ) {
        super(distance, sectionTime, BUS);
        this.busNo = busNo;
        this.busType = busType;

        this.startPlaceName = startPlaceName;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;

        this.endPlaceName = endPlaceName;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
    }
}
