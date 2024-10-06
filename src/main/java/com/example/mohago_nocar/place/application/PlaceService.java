package com.example.mohago_nocar.place.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.presentation.response.FestivalLocationResponseDto;
import com.example.mohago_nocar.place.application.converter.PlaceConverter;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceImageRepository;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.place.domain.service.PlaceUseCase;
import com.example.mohago_nocar.place.infrastructure.externalApi.GoogleApiClient;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService implements PlaceUseCase {

    private final GoogleApiClient googleApiClient;
    private final FestivalUseCase festivalUseCase;
    private final FestivalNearPlaceRepository festivalNearPlaceRepository;
    private final FestivalNearPlaceImageRepository festivalNearPlaceImageRepository;

    private static final int RADIUS = 1500;

    @Override
    @Transactional
    public void updateAllFestivalNearbyPlaces() {
        List<Festival> festivals = festivalUseCase.getAllFestivals();
        festivals.forEach(festival -> updateFestivalNearbyPlaces(festival.getId()));
    }

    private void updateFestivalNearbyPlaces(Long festivalId) {
        FestivalLocationResponseDto festivalLocation = festivalUseCase.getFestivalLocation(festivalId);
        List<PlaceResponseDto> nearbyPlaces = getNearbyPlacesFrom(festivalLocation);

        saveNearbyPlaces(festivalId, nearbyPlaces);
    }

    private List<PlaceResponseDto> getNearbyPlacesFrom(FestivalLocationResponseDto festivalLocation) {
        return googleApiClient.findNearbyPlaces(
                festivalLocation.location().getLatitude(),
                festivalLocation.location().getLongitude(),
                RADIUS
        );
    }

    private void saveNearbyPlaces(Long festivalId, List<PlaceResponseDto> nearbyPlaces) {
        nearbyPlaces.forEach(placeDto -> {
            FestivalNearPlace savedPlace = saveFestivalNearPlace(festivalId, placeDto);
            saveFestivalNearPlaceImages(savedPlace.getId(), placeDto.getPhotos());
        });
    }

    private FestivalNearPlace saveFestivalNearPlace(Long festivalId, PlaceResponseDto placeDto) {
        FestivalNearPlace place = PlaceConverter.convertToFestivalNearPlace(festivalId, placeDto);
        return festivalNearPlaceRepository.save(place);
    }

    private void saveFestivalNearPlaceImages(Long placeId, List<String> photos) {
        List<FestivalNearPlaceImage> placeImages = PlaceConverter.convertToFestivalNearPlaceImage(placeId, photos);
        placeImages.forEach(festivalNearPlaceImageRepository::save);
    }
}