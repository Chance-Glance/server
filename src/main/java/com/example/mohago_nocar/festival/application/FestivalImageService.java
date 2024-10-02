package com.example.mohago_nocar.festival.application;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import com.example.mohago_nocar.festival.domain.repository.FestivalImageRepository;
import com.example.mohago_nocar.festival.domain.service.FestivalImageUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalImageService implements FestivalImageUseCase {

    private final FestivalImageRepository festivalImageRepository;

    @Override
    public List<FestivalImage> getAllFestivalImages(Long festivalId) {
        return festivalImageRepository.getAllFestivalImages(festivalId);
    }
}
