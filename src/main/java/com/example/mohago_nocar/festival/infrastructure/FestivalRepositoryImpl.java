package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import java.util.List;
import com.example.mohago_nocar.festival.presentation.exception.FestivalNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalRepositoryImpl implements FestivalRepository {

    private final FestivalJpaRepository festivalJpaRepository;

    @Override
    public Page<Festival> getFestivals(Pageable pageable) {
        return festivalJpaRepository.findAll(pageable);
    }

    @Override
    public Festival getFestivalById(Long id) {
        return festivalJpaRepository.findById(id)
                .orElseThrow(FestivalNotFoundException::new);
    }

    @Override
    public List<Festival> getAllFestivals() {
        return festivalJpaRepository.findAll();
    }
}
