package com.example.mohago_nocar.festival.domain.repository;

import com.example.mohago_nocar.festival.domain.model.Festival;

import java.time.LocalDate;
import java.util.List;

public interface FestivalDateRepository {
    List<Festival> getFestivalDateBetween(LocalDate startDate, LocalDate endDate);

}
