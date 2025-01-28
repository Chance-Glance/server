package com.example.mohago_nocar.transit.infrastructure.externalApi.odsay.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record OdsayRouteResponse(
        JsonNode result,
        boolean isTooShortDistance
) {

    public static OdsayRouteResponse of(JsonNode result, boolean isTooShortDistance) {
        return OdsayRouteResponse.builder()
                .result(result)
                .isTooShortDistance(isTooShortDistance)
                .build();
    }

}
