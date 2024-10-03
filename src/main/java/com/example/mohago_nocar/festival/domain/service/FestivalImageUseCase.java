package com.example.mohago_nocar.festival.domain.service;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import java.util.List;

public interface FestivalImageUseCase {

    List<FestivalImage> getAllFestivalImages(Long festivalId);
}
