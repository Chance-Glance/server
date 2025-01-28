package com.example.mohago_nocar.transit.infrastructure.externalApi.odsay;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class ResponseValidator {

    public static boolean hasError(JsonNode response) {
        return find(response, "error").isPresent();
    }

    private static Optional<JsonNode> find(JsonNode node, String fieldName) {
        try {
            JsonNode result = node.get(fieldName);
            return Optional.of(result);
        } catch (NullPointerException exception) {
            return Optional.empty();
        }
    }

}
