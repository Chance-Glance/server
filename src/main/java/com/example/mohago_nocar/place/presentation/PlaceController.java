package com.example.mohago_nocar.place.presentation;

import com.example.mohago_nocar.festival.domain.service.FestivalUseCase;
import com.example.mohago_nocar.festival.presentation.response.FestivalResponseDto;
import com.example.mohago_nocar.global.common.dto.PagedResponseDto;
import com.example.mohago_nocar.global.common.response.ApiResponse;
import com.example.mohago_nocar.place.domain.service.PlaceUseCase;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.GoogleNearByPlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@Tag(name = "Place", description = "축제 주변 장소")
public class PlaceController {

    private final PlaceUseCase placeUseCase;

    @Operation(summary = "축제 주변 장소 조회", description = "축제 주변 장소 정보를 반환합니다. 페이지네이션이 적용되어있습니다.")
    @GetMapping("/{festivalId}")
    public ApiResponse<GoogleNearByPlaceResponse> fetchPlaces(
            @PathVariable(name = "festivalId") Long festivalId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        GoogleNearByPlaceResponse pagedResponse = placeUseCase.fetchPlaces(festivalId, pageable);
        return ApiResponse.ok(pagedResponse);
    }
}
