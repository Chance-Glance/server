package com.example.mohago_nocar.festival.presentation;

import com.example.mohago_nocar.festival.application.FestivalDateService;
import com.example.mohago_nocar.festival.presentation.response.FestivalActivePeriodResponseDto;
import com.example.mohago_nocar.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
@RequiredArgsConstructor
@Tag(name = "Festival", description = "축제")
public class FestivalDateController {

    private final FestivalDateService festivalDateService;

    @Operation(summary = "축제 일정 조회", description = "지정된 축제의 일정을 조회합니다.")
    @GetMapping("/active-period")
    public ApiResponse<FestivalActivePeriodResponseDto> findFestivalDate(
            @RequestParam(name = "id") Long id
    ) {
        FestivalActivePeriodResponseDto activePeriodResponse = festivalDateService.getFestivalActivePeriodById(id);

        return ApiResponse.ok(activePeriodResponse);
    }
}
