package com.example.mohago_nocar.transit.infrastructure.externalApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public record OdsayResponse(
        Optional<JsonNode> error,
        Optional<JsonNode> result
) {
}
