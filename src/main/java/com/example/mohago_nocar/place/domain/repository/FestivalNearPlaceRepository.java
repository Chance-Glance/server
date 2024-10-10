package com.example.mohago_nocar.place.domain.repository;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalNearPlaceRepository {

    FestivalNearPlace save(FestivalNearPlace place);

    Page<FestivalNearPlace> getFestivalNearPlaceByFestivalId(Long festivalId, Pageable pageable);
}
