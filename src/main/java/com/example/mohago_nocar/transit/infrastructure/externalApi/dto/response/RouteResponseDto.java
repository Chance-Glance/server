package com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record RouteResponseDto(
        JsonNode result
) {
    public static RouteResponseDto of(JsonNode result) {
        return RouteResponseDto.builder()
                .result(result)
                .build();
    }
}
