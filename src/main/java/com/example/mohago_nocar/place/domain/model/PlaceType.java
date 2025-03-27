package com.example.mohago_nocar.place.domain.model;

public enum PlaceType {
    RESTAURANT, ATTRACTION;

    public static PlaceType from(String placeType) {
        if (placeType.contains("음식점")
                || placeType.contains("식당")
                || placeType.contains("카페")
                || placeType.contains("커피숍/커피 전문점")
                || placeType.contains("제과점")
                || placeType.contains("아이스크림 가게")
        ) {
            return RESTAURANT;
        }

        return ATTRACTION;
    }
}
