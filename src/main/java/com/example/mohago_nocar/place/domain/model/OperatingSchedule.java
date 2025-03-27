package com.example.mohago_nocar.place.domain.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class OperatingSchedule {

    private static final int MAX_DAYS_IN_WEEK = 7;

    @ElementCollection
    @CollectionTable(name = "operating_hours", joinColumns = @JoinColumn(name = "festival_near_place_id"))
    private List<OperatingHour> schedule = new ArrayList<>();

    public List<OperatingHour> getOperatingHours() {
        return schedule;
    }

    public static OperatingSchedule from(List<String> operatingHours) {
        if (operatingHours.size() > MAX_DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Operating hours cannot exceed " + MAX_DAYS_IN_WEEK + " days.");
        }

        OperatingSchedule operatingSchedule = new OperatingSchedule();
        operatingHours.forEach(operatingSchedule::addOperatingHours);

        return operatingSchedule;
    }

    private void addOperatingHours(String operatingHour) {
        schedule.add(OperatingHour.from(operatingHour));
    }


    @Embeddable
    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class OperatingHour {

        private String operatingHour;

        public static OperatingHour from(String operatingHour) {
            return OperatingHour.builder()
                    .operatingHour(operatingHour)
                    .build();
        }

        @Builder
        private OperatingHour(String operatingHour) {
            this.operatingHour = operatingHour;
        }
    }
}
