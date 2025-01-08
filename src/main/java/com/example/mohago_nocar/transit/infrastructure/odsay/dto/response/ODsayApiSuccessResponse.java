package com.example.mohago_nocar.transit.infrastructure.odsay.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record ODsayApiSuccessResponse(
        JsonNode result,
        boolean isTooShortDistance
) {

    public static ODsayApiSuccessResponse of(JsonNode result, boolean isTooShortDistance) {
        return ODsayApiSuccessResponse.builder()
                .result(result)
                .isTooShortDistance(isTooShortDistance)
                .build();
    }

}
