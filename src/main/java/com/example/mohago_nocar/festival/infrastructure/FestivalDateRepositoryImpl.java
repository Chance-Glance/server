package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FestivalDateRepositoryImpl implements FestivalDateRepository {
    private final FestivalDateJpaRepository festivalDateJpaRepository;

    @Override
    public List<Festival> getFestivalDateBetween(LocalDate startDate, LocalDate endDate) {
        return festivalDateJpaRepository.findByFestivalDateBetween(startDate, endDate);
    }
}
