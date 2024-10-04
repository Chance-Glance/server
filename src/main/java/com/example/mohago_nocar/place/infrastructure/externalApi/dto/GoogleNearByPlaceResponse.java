package com.example.mohago_nocar.place.infrastructure.externalApi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;

public record GoogleNearByPlaceResponse(
        Optional<JsonNode> results
) {
}