package com.example.mohago_nocar.festival.domain.repository;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import java.util.List;

public interface FestivalImageRepository {

    List<FestivalImage> getAllFestivalImages(Long festivalId);
}
