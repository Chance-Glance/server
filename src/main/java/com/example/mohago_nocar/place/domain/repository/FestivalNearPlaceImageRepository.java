package com.example.mohago_nocar.place.domain.repository;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import java.util.List;

public interface FestivalNearPlaceImageRepository {

    FestivalNearPlaceImage save(FestivalNearPlaceImage image);

    List<FestivalNearPlaceImage> getAllPlaceImageByPlaceId(Long id);
}
