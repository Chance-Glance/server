package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.global.common.exception.EntityNotFoundException;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

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

    @Override
    public String getPlaceNameByLocation(Location location) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<String> result = festivalNearPlaceJpaRepository.findNamesByLocation(location.getLatitude(), location.getLongitude(), pageRequest);
        return result.getContent().get(0);
    }
}
