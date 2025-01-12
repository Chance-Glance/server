package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.global.common.exception.EntityNotFoundException;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mohago_nocar.place.presentation.exception.PlaceErrorCode.PLACE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class FestivalNearPlaceRepositoryImpl implements FestivalNearPlaceRepository {

    private final FestivalNearPlaceJpaRepository festivalNearPlaceJpaRepository;

    @Override
    public FestivalNearPlace save(FestivalNearPlace place) {
        return festivalNearPlaceJpaRepository.save(place);
    }

    @Override
    public FestivalNearPlace findById(Long id) {
        return festivalNearPlaceJpaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(PLACE_NOT_FOUND));
    }

    public Page<FestivalNearPlace> getFestivalNearPlaceByFestivalId(Long festivalId, Pageable pageable) {
        return festivalNearPlaceJpaRepository.findAllByFestivalId(festivalId, pageable);
    }

    @Override
    public List<FestivalNearPlace> findByFestivalId(Long festivalId) {
        return festivalNearPlaceJpaRepository.findAllByFestivalId(festivalId);
    }

}
