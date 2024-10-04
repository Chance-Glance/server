package com.example.mohago_nocar.place.application;

import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.presentation.response.FestivalLocationResponseDto;
import com.example.mohago_nocar.place.domain.service.PlaceUseCase;
import com.example.mohago_nocar.place.infrastructure.externalApi.GoogleApiClient;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.GoogleNearByPlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService implements PlaceUseCase {

    private final GoogleApiClient googleApiClient;
    private final FestivalUseCase festivalUseCase;

    @Override
    public GoogleNearByPlaceResponse fetchPlaces(Long festivalId, Pageable pageable) {
        FestivalLocationResponseDto responseDto = festivalUseCase.getFestivalLocation(festivalId);
        return googleApiClient.request(responseDto.location().getLatitude(), responseDto.location().getLongitude(), 1500);
    }
}
