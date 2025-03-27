package com.example.mohago_nocar.place.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.presentation.response.FestivalLocationResponseDto;
import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import com.example.mohago_nocar.place.application.mapper.FestivalNearPlaceMapper;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import com.example.mohago_nocar.place.domain.model.OperatingSchedule;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceImageRepository;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import com.example.mohago_nocar.place.domain.service.PlaceUseCase;
import com.example.mohago_nocar.place.infrastructure.externalApi.GoogleApiClient;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import com.example.mohago_nocar.place.presentation.NearPlaceResponseDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NearPlaceResponseDto> getFestivalNearPlaces(Long festivalId, Pageable pageable) {
        Festival festival = festivalUseCase.getFestival(festivalId);
        Page<FestivalNearPlace> pagedPlaces = festivalNearPlaceRepository.getFestivalNearPlaceByFestivalId(festivalId, pageable);
        Page<NearPlaceResponseDto> nearPlaceResponseDtos = pagedPlaces.map(this::convertPlaceToNearPlaceResponseDto);
        return new PagedResponseDto<>(nearPlaceResponseDtos);
    }

    private NearPlaceResponseDto convertPlaceToNearPlaceResponseDto(FestivalNearPlace place) {
        List<FestivalNearPlaceImage> images = festivalNearPlaceImageRepository.getAllPlaceImageByPlaceId(place.getId());
        List<String> imageUrls = images.stream().map(FestivalNearPlaceImage::getImageUrl).toList();
        List<String> operatingHours = place.getOperatingSchedule().getOperatingHours().stream().map(OperatingSchedule.OperatingHour::getOperatingHour).toList();
        return NearPlaceResponseDto.of(place, operatingHours, imageUrls);
    }

    private void updateFestivalNearbyPlaces(Long festivalId) {
        FestivalLocationResponseDto festivalLocation = festivalUseCase.getFestivalLocation(festivalId);
        List<PlaceResponseDto> nearbyPlaces = searchNearbyPlacesWithImages(festivalLocation);
        saveNearbyPlacesWithImages(festivalId, nearbyPlaces);
    }

    private List<PlaceResponseDto> searchNearbyPlacesWithImages(FestivalLocationResponseDto festivalLocation) {
        return googleApiClient.searchNearbyPlacesWithImageUris(
                festivalLocation.location().getLatitude(),
                festivalLocation.location().getLongitude(),
                RADIUS
        );
    }

    private void saveNearbyPlacesWithImages(Long festivalId, List<PlaceResponseDto> nearbyPlaces) {
        nearbyPlaces.forEach(placeDto -> {
            FestivalNearPlace savedPlace = saveFestivalNearPlace(festivalId, placeDto);
            saveFestivalNearPlaceImages(savedPlace.getId(), placeDto.getPhotos());
        });
    }

    private FestivalNearPlace saveFestivalNearPlace(Long festivalId, PlaceResponseDto placeDto) {
        FestivalNearPlace place = FestivalNearPlaceMapper.convertToFestivalNearPlace(festivalId, placeDto);
        return festivalNearPlaceRepository.save(place);
    }

    private void saveFestivalNearPlaceImages(Long placeId, List<String> photos) {
        List<FestivalNearPlaceImage> placeImages = FestivalNearPlaceMapper.convertToFestivalNearPlaceImage(placeId, photos);
        placeImages.forEach(festivalNearPlaceImageRepository::save);
    }

}