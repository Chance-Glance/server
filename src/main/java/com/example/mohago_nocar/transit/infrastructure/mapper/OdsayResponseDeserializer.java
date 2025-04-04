package com.example.mohago_nocar.transit.infrastructure.mapper;

import com.example.mohago_nocar.global.common.exception.InternalServerException;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.OdsaySearchRouteResponseDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import java.io.IOException;
import java.util.Optional;

@JsonComponent
@Slf4j
public class OdsayResponseDeserializer extends JsonDeserializer<OdsaySearchRouteResponseDto> {

    @Override
    public OdsaySearchRouteResponseDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            return parse(node);
        } catch (IOException exception) {
            log.error("IOException 발생");
            throw new InternalServerException(exception.getMessage());
        }
    }

    private OdsaySearchRouteResponseDto parse(JsonNode node) {
        Optional<JsonNode> errorNode = find(node, "error");
        Optional<JsonNode> result = find(node, "result");

        return new OdsaySearchRouteResponseDto(errorNode, result);
    }

    private Optional<JsonNode> find(JsonNode node, String fieldName) {
        try {
            JsonNode result = node.get(fieldName);
            return Optional.of(result);
        } catch (NullPointerException exception) {
            return Optional.empty();
        }
    }
}
