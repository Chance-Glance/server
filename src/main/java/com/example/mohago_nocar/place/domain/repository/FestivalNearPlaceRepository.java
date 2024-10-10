package com.example.mohago_nocar.place.domain.repository;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;

public interface FestivalNearPlaceRepository {

    FestivalNearPlace save(FestivalNearPlace place);

    FestivalNearPlace findByGooglePlaceId(String googlePlaceId);
}
