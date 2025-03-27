package com.example.mohago_nocar.place.infrastructure.externalApi.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

public record GoogleNearbyPlaceResponse(
        JsonNode places
) {
}