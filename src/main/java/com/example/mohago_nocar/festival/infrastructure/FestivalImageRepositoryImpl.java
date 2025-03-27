package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import com.example.mohago_nocar.festival.domain.repository.FestivalImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalImageRepositoryImpl implements FestivalImageRepository {

    private final FestivalImageJpaRepository festivalImageJpaRepository;

    @Override
    public List<FestivalImage> getAllFestivalImages(Long festivalId) {
        return festivalImageJpaRepository.findAllByFestivalId(festivalId);
    }
}
