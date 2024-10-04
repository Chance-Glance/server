package com.example.mohago_nocar.place.domain.service;

import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.GoogleNearByPlaceResponse;
import org.springframework.data.domain.Pageable;

public interface PlaceUseCase {

    GoogleNearByPlaceResponse fetchPlaces(Long festivalId, Pageable pageable);
}
