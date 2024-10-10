package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalNearPlaceImageRepositoryImpl implements FestivalNearPlaceImageRepository {

    private final FestivalNearPlaceImageJpaRepository festivalNearPlaceImageJpaRepository;

    @Override
    public FestivalNearPlaceImage save(FestivalNearPlaceImage image) {
        return festivalNearPlaceImageJpaRepository.save(image);
    }
}
