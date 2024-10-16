package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.transit.domain.model.PathType;
import com.example.mohago_nocar.transit.domain.model.SubPath;
import com.example.mohago_nocar.transit.domain.model.WalkPath;
import lombok.Builder;
import lombok.Getter;

import static com.example.mohago_nocar.transit.domain.model.PathType.WALK;

@Getter
public class WalkPathResponseDto extends SubPathResponseDto{

    public static WalkPathResponseDto of(SubPath subPath) {
        WalkPath walkPath = (WalkPath) subPath;

        return WalkPathResponseDto.builder()
                .distance(walkPath.getDistance())
                .sectionTime(walkPath.getSectionTime())
                .build();
    }

    @Builder
    private WalkPathResponseDto(double distance, int sectionTime) {
        super(distance, sectionTime, WALK);
    }
}
