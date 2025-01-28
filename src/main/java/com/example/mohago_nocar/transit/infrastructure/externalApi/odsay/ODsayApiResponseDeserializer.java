package com.example.mohago_nocar.transit.infrastructure.externalApi.odsay;

import com.example.mohago_nocar.global.common.exception.InternalServerException;
import com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayBadRequestException;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayServerException;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayTooManyRequestsException;
import com.example.mohago_nocar.transit.infrastructure.externalApi.odsay.dto.response.OdsayRouteResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@JsonComponent
@Slf4j
public class ODsayApiResponseDeserializer extends JsonDeserializer<OdsayRouteResponse> {

    private static final boolean TOO_SHORT_DISTANCE = true;
    private static final boolean NORMAL_DISTANCE = false;

    @Override
    public OdsayRouteResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            JsonNode response = parseJsonResponse(jsonParser);
            return processResponse(response);
        } catch (IOException exception) {
            log.error("IOException 발생");
            throw new InternalServerException(exception.getMessage());
        }
    }

    private OdsayRouteResponse processResponse(JsonNode response) {
        return ResponseValidator.hasError(response) ?
                handleErrorResponse(response) :
                handleSuccessResponse(response);
    }

    private OdsayRouteResponse handleErrorResponse(JsonNode response) {
        OdsayErrorCode errorCode = extractErrorCode(response);

        if (errorCode.isServerError()) {
            log.error("ODsay API server error :{}", errorCode.getMessage());
            throw new OdsayServerException();
        }

        if (errorCode.isDistanceError()) {
            return createTooShortDistanceResponse();
        }

        if (errorCode.isTooManyRequests()) {
            throw new OdsayTooManyRequestsException();
        }

        log.error("ODsay API bad request error :{}", errorCode.getMessage());
        throw new OdsayBadRequestException(errorCode);
    }

    private OdsayRouteResponse handleSuccessResponse(JsonNode response) {
        return createSuccessResponse(response);
    }

    /**
     * 출, 도착지 사이 거리가 700m 이내여서 발생하는 에러를 성공 응답으로 변환합니다.
     */
    private OdsayRouteResponse createTooShortDistanceResponse() {
        return OdsayRouteResponse.of(null, TOO_SHORT_DISTANCE);
    }

    private OdsayRouteResponse createSuccessResponse(JsonNode node) {
        Optional<JsonNode> result = find(node, "result");

        if (result.isEmpty()) {
            log.error("[ODsay] result 바인딩을 실패하였습니다.");
            log.error("ODsay API response : {}", node.toPrettyString());
            throw new InternalServerException("ODsay API result 바인딩에 실패하였습니다.");
        }

        return OdsayRouteResponse.of(result.get(), NORMAL_DISTANCE);
    }

    private JsonNode parseJsonResponse(JsonParser jsonParser) throws IOException {
        return jsonParser.getCodec().readTree(jsonParser);
    }

    private OdsayErrorCode extractErrorCode(JsonNode response) {
        JsonNode errorResponse = find(response, "error").get();

        String errorCode = extractErrorInfo(errorResponse, "code");
        String errorMessage = extractErrorInfo(errorResponse, "message", "msg");
        OdsayErrorCode odsayErrorCode = OdsayErrorCode.from(errorCode);

        log.warn("ODsay errorMessage: {}", errorMessage);
        log.warn("ODsay API returns error response : {}", odsayErrorCode);

        return odsayErrorCode;
    }

    private String extractErrorInfo(JsonNode errorNode, String... fields) {
        return Arrays.stream(fields)
                .map(errorNode::findPath)
                .filter(node -> !node.isMissingNode())
                .findFirst()
                .map(JsonNode::asText)
                .orElse(null);
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
