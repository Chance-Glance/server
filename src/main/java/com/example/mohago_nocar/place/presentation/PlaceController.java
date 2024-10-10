package com.example.mohago_nocar.place.presentation;

import com.example.mohago_nocar.global.common.response.ApiResponse;
import com.example.mohago_nocar.place.domain.service.PlaceUseCase;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nearby-places")
@RequiredArgsConstructor
@Tag(name = "Place", description = "축제 주변 장소")
public class PlaceController {

    private final PlaceUseCase placeUseCase;

    @Operation(summary = "축제 주변 장소 업데이트", description = "축제 주변 장소 정보를 업데이트합니다. ")
    @PatchMapping("/update")
    public ApiResponse<List<PlaceResponseDto>> updateFestivalNearPlaces() {
        placeUseCase.updateAllFestivalNearbyPlaces();
        return ApiResponse.ok(null);
    }
}
