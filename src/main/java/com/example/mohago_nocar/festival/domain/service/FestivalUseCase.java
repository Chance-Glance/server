package com.example.mohago_nocar.festival.domain.service;

import com.example.mohago_nocar.festival.presentation.response.FestivalResponseDto;
import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import org.springframework.data.domain.Pageable;

public interface FestivalUseCase {

    PagedResponseDto<FestivalResponseDto> fetchFestivals(Pageable pageable);
}
