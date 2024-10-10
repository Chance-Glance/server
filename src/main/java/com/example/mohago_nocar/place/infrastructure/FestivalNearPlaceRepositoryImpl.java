package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.global.common.exception.EntityNotFoundException;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.mohago_nocar.place.presentation.exception.PlaceErrorCode.GOOGLE_PLACE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class FestivalNearPlaceRepositoryImpl implements FestivalNearPlaceRepository {

    private final FestivalNearPlaceJpaRepository festivalNearPlaceJpaRepository;

    @Override
    public FestivalNearPlace save(FestivalNearPlace place) {
        return festivalNearPlaceJpaRepository.save(place);
    }

    @Override
    public FestivalNearPlace findByGooglePlaceId(String googlePlaceId) {
        return festivalNearPlaceJpaRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(()-> new EntityNotFoundException(GOOGLE_PLACE_NOT_FOUND));
    }
}
