package com.example.mohago_nocar.transit.infrastructure.mapper;

import com.example.mohago_nocar.global.common.exception.InternalServerException;
import com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayDistanceException;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayException;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.OdsaySearchRouteResponseDto;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.RouteResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


@Slf4j
public class OdsayRouteMapper {

    public static RouteResponseDto mapOdsayRouteResponseToRouteResponse(OdsaySearchRouteResponseDto response) {
        checkForOdsayErrors(response);

        return response.result()
                .map(RouteResponseDto::of)
                .orElseThrow(() -> new InternalServerException("ODsay API Result is missing"));
    }

    private static void checkForOdsayErrors(OdsaySearchRouteResponseDto response) {
        response.error().ifPresent(errorNode -> {
            String errorCode = extractErrorInfo(errorNode, "code");
            String errorMessage = extractErrorInfo(errorNode, "message", "msg");

            if (errorNode == null) {
                log.error("에러 응답 바인딩을 실패하였습니다.");
                log.warn("error : {}", errorNode.toPrettyString());
                throw new InternalServerException("ODsay API error 처리에 실패하였습니다.");
            }

            OdsayErrorCode odsayErrorCode = OdsayErrorCode.from(errorCode);

            log.info("ODsay errorMessage: {}", errorMessage);
            log.warn("ODsay API returns error response : {}", odsayErrorCode);

            if (odsayErrorCode.isDistanceException()) {
                throw new OdsayDistanceException();
            }

            throw new OdsayException(odsayErrorCode);
        });
    }

    private static String extractErrorInfo(JsonNode errorNode, String... fields) {
        return Arrays.stream(fields)
                .map(errorNode::findPath)
                .filter(node -> !node.isMissingNode())
                .findFirst()
                .map(JsonNode::asText)
                .orElse(null);
    }
}
