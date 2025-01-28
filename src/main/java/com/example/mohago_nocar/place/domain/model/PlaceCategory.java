package com.example.mohago_nocar.place.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum PlaceCategory {

    ATTRACTION("관광명소", "AT4"),
    FOOD("음식점", "FD6"),
    CULTURAL_FACILITIES("문화시설", "CT1");

    private final String name;
    private final String code;

    public static List<PlaceCategory> getTravelCategories() {
        return List.of(ATTRACTION, FOOD, CULTURAL_FACILITIES);
    }

    public static PlaceCategory getCategoryByCode(String code) {
        if (code.equals(ATTRACTION.getCode())) {
            return ATTRACTION;
        }

        if (code.equals(FOOD.getCode())) {
            return FOOD;
        }

        if (code.equals(CULTURAL_FACILITIES.getCode())) {
            return CULTURAL_FACILITIES;
        }

        return null;
    }

}
