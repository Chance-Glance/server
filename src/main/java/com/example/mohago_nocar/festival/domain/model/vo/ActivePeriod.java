package com.example.mohago_nocar.festival.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivePeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    public static ActivePeriod from(LocalDate startDate, LocalDate endDate) {
        return ActivePeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
