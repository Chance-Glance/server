package com.example.mohago_nocar.place.domain.model;

public enum PlaceType {
    RESTAURANT, ATTRACTION;

    public static PlaceType from(String placeType) {
        return switch (placeType.toLowerCase()) {
            case
                    "restaurant",
                    "barbecue_restaurant",
                    "bakery",
                    "breakfast_restaurant",
                    "brunch_restaurant",
                    "cafe",
                    "chinese_restaurant",
                    "coffee_shop",
                    "fast_food_restaurant",
                    "french_restaurant",
                    "hamburger_restaurant",
                    "ice_cream_shop",
                    "indian_restaurant",
                    "indonesian_restaurant",
                    "italian_restaurant",
                    "japanese_restaurant",
                    "korean_restaurant",
                    "mediterranean_restaurant",
                    "mexican_restaurant",
                    "middle_eastern_restaurant",
                    "pizza_restaurant",
                    "ramen_restaurant",
                    "sandwich_shop",
                    "seafood_restaurant",
                    "steak_house",
                    "sushi_restaurant"
                    -> RESTAURANT;

            default -> ATTRACTION;
        };
    }
}
