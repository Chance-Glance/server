package com.example.mohago_nocar.festival.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.festival.presentation.exception.FestivalNotFoundException;
import com.example.mohago_nocar.festival.presentation.response.FestivalActivePeriodResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalDateService {

    private final FestivalRepository festivalRepository;

    public FestivalActivePeriodResponseDto getFestivalActivePeriodById(Long festivalId) {
        Festival festival = festivalRepository.getFestivalById(festivalId)
                .orElseThrow(FestivalNotFoundException::new);
        return FestivalActivePeriodResponseDto.of(festival.getActivePeriod());
    }
}
