package com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public record OdsaySearchRouteResponseDto(
        Optional<JsonNode> error,
        Optional<JsonNode> result
) {
}
