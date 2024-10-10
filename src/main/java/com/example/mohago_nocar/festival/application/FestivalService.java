package com.example.mohago_nocar.festival.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import com.example.mohago_nocar.festival.domain.repository.FestivalImageRepository;
import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import com.example.mohago_nocar.festival.domain.service.FestivalImageUseCase;
import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.infrastructure.FestivalImageJpaRepository;
import com.example.mohago_nocar.festival.presentation.exception.FestivalNotFoundException;
import com.example.mohago_nocar.festival.presentation.response.FestivalLocationResponseDto;
import com.example.mohago_nocar.festival.presentation.response.FestivalResponseDto;
import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalService implements FestivalUseCase {

    private final FestivalRepository festivalRepository;
    private final FestivalImageUseCase festivalImageUseCase;

    @Override
    public PagedResponseDto<FestivalResponseDto> fetchFestivals(Pageable pageable) {
        Page<Festival> pagedFestival = festivalRepository.getFestivals(pageable);
        Page<FestivalResponseDto> pagedFestivalResponseDto =
                pagedFestival.map(festival -> {
                    List<FestivalImage> festivalImages = festivalImageUseCase.getAllFestivalImages(festival.getId());
                    List<String> festivalImagesUrl = festivalImages.stream().map(FestivalImage::getImageUrl).toList();
                    return FestivalResponseDto.of(festival, festivalImagesUrl);
                    }
                );
        return new PagedResponseDto<>(pagedFestivalResponseDto);
    }

    @Override
    public FestivalLocationResponseDto getFestivalLocation(Long festivalId) {
        Festival festival = festivalRepository.getFestivalById(festivalId);

        return FestivalLocationResponseDto.of(festival.getLocation());
    }

    @Override
    public List<Festival> getAllFestivals() {

        return festivalRepository.getAllFestivals();
    }
}
