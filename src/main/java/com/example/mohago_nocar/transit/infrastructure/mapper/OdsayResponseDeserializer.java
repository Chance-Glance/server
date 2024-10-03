package com.example.mohago_nocar.transit.infrastructure.mapper;

import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.OdsayResponse;
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
public class OdsayResponseDeserializer extends JsonDeserializer<OdsayResponse> {

    @Override
    public OdsayResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            return parse(node);
        } catch (IOException exception) {
            // TODO: Exception 핸들링 코드 완성 이후 커스텀 에러로 변경
            throw new RuntimeException(exception.getMessage());
        }
    }

    private OdsayResponse parse(JsonNode node) {
        Optional<JsonNode> errorNode = find(node, "error");
        Optional<JsonNode> result = find(node, "result");

        return new OdsayResponse(errorNode, result);
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
