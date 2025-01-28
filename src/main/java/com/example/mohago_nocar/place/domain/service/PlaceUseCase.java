package com.example.mohago_nocar.place.domain.service;

import com.example.mohago_nocar.place.presentation.NearPlaceResponseDto;

import java.util.List;

public interface PlaceUseCase {

    List<NearPlaceResponseDto> getFestivalNearPlaces(Long festivalId);

}
