package com.example.mohago_nocar.place.infrastructure.externalApi.dto.request;

import lombok.Builder;
import java.util.List;


@Builder
public record GoogleNearPlaceRequest(
        List<String> includedTypes,
        int maxResultCount,
        LocationRestriction locationRestriction,
        String rankPreference,
        String languageCode
) {
    public static GoogleNearPlaceRequest of(
            List<String> includedTypes,
            int maxResultCount,
            double latitude,
            double longitude,
            double radius,
            String rankPreference,
            String languageCode
    ) {
        return GoogleNearPlaceRequest.builder()
                .includedTypes(includedTypes)
                .maxResultCount(maxResultCount)
                .locationRestriction(LocationRestriction.of(latitude, longitude, radius))
                .rankPreference(rankPreference)
                .languageCode(languageCode)
                .build();
    }
}
