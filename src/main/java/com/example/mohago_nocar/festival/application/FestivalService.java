package com.example.mohago_nocar.festival.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.presentation.response.FestivalResponseDto;
import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalService implements FestivalUseCase {

    public final FestivalRepository festivalRepository;

    @Override
    public PagedResponseDto<FestivalResponseDto> fetchFestivals(Pageable pageable) {
        Page<Festival> pagedFestival = festivalRepository.getFestivals(pageable);
        Page<FestivalResponseDto> pagedFestivalResponseDto =  pagedFestival.map(FestivalResponseDto::of);
        return new PagedResponseDto<>(pagedFestivalResponseDto);
    }
}
