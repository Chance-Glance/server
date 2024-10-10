package com.example.mohago_nocar.festival.application;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.festival.domain.repository.FestivalDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalDateService {

    private final FestivalDateRepository festivalDateRepository;

    public List<LocalDate> findFestivalsBetween(LocalDate startDate, LocalDate endDate) {
        List<Festival> festivals = festivalDateRepository.getFestivalDateBetween(startDate, endDate);

        List<LocalDate> festivalDates = new ArrayList<>();

        for (Festival festival : festivals) {
            LocalDate festivalStart = festival.getStartDate();
            LocalDate festivalEnd = festival.getEndDate();

            while (!festivalStart.isAfter(festivalEnd)) {
                if (!festivalStart.isBefore(startDate) && !festivalStart.isAfter(endDate)) {
                    festivalDates.add(festivalStart);
                }
                festivalStart = festivalStart.plusDays(1);
            }
        }
        return festivalDates;
    }
}
