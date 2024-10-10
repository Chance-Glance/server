package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalNearPlaceRepositoryImpl implements FestivalNearPlaceRepository {

    private final FestivalNearPlaceJpaRepository festivalNearPlaceJpaRepository;

    @Override
    public FestivalNearPlace save(FestivalNearPlace place) {
        return festivalNearPlaceJpaRepository.save(place);
    }

    @Override
    public Page<FestivalNearPlace> getFestivalNearPlaceByFestivalId(Long festivalId, Pageable pageable) {
        return festivalNearPlaceJpaRepository.findAllByFestivalId(festivalId, pageable);
    }
}
