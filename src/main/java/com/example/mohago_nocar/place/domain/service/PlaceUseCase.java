package com.example.mohago_nocar.place.domain.service;

import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import com.example.mohago_nocar.place.presentation.NearPlaceResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceUseCase {

    void updateAllFestivalNearbyPlaces();

    PagedResponseDto<NearPlaceResponseDto> getFestivalNearPlaces(Long festivalId, Pageable pageable);
}
