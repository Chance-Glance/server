package com.example.mohago_nocar.festival.presentation;

import com.example.mohago_nocar.festival.application.FestivalDateService;
import com.example.mohago_nocar.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/festivals")
@RequiredArgsConstructor
@Tag(name = "Festival", description = "축제")
public class FestivalDateController {

    private final FestivalDateService festivalDateService;

    @Operation(summary = "축제 일정 조회", description = "지정된 날짜 범위 내의 축제 일정을 조회합니다.")
    @GetMapping("/dates")
    public ApiResponse<List<LocalDate>> fetchFestivals(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        List<LocalDate> festivalDates = festivalDateService.findFestivalsBetween(startDate, endDate);
        return ApiResponse.ok(festivalDates);
    }
}
