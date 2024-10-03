package com.example.mohago_nocar.festival.presentation.response;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.model.vo.ActivePeriod;
import com.example.mohago_nocar.festival.domain.model.vo.Location;
import java.util.List;
import lombok.Builder;

@Builder
public record FestivalResponseDto(
        Long id,
        String name,
        ActivePeriod activePeriod,
        String description,
        String address,
        Location location,
        List<String> imageUrlList
) {

    public static FestivalResponseDto of(Festival festival, List<String> imageUrlList) {
        return new FestivalResponseDtoBuilder()
                .id(festival.getId())
                .name(festival.getName())
                .activePeriod(festival.getActivePeriod())
                .description(festival.getDescription())
                .address(festival.getAddress())
                .location(festival.getLocation())
                .imageUrlList(imageUrlList)
                .build();
    }
}
