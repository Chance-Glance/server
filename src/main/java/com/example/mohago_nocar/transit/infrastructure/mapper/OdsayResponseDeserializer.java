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
        Optional<String> code = find(node, "code");
        Optional<String> message = find(node, "message", "msg");
        Optional<JsonNode> result = findResult(node);

        return new OdsayResponse(code, message, result);
    }

    private Optional<String> find(JsonNode node, String... fieldName) {
        for (String field : fieldName) {
            JsonNode nodeName = node.findPath(field);
            if (!nodeName.isMissingNode()) {
                return Optional.of(nodeName.textValue());
            }
        }
        return Optional.empty();
    }

    private Optional<JsonNode> findResult(JsonNode node) {
        try {
            JsonNode result = node.get("result");
            return Optional.of(result);
        } catch (NullPointerException exception) {
            return Optional.empty();
        }
    }
}
