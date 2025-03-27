package com.example.mohago_nocar.place.domain.repository;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalNearPlaceRepository {

    FestivalNearPlace save(FestivalNearPlace place);

    FestivalNearPlace findById(Long id);

    String getPlaceNameByLocation(Location location);

    Page<FestivalNearPlace> getFestivalNearPlaceByFestivalId(Long festivalId, Pageable pageable);
}
