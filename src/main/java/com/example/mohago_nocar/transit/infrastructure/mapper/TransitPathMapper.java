package com.example.mohago_nocar.transit.infrastructure.mapper;

import com.example.mohago_nocar.transit.domain.model.*;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.OdsayResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TransitPathMapper {

    private static final String ODSAY_SERVER_ERROR = "500";

    public static TransitInfo mapToTransitInfo(OdsayResponse response) {
        validateOdsayErrorResponse(response);

        // TODO: Exception 핸들링 코드 완성 이후 커스텀 에러로 변경
        JsonNode result = response.result().orElseThrow(() -> new RuntimeException("Result is missing"));

        JsonNode path = extractPath(result);

        double totalDistance = extractTotalDistance(path);
        int totalTime = extractTotalTime(path);

        JsonNode subPathsNode = extractSubPathsNode(path);

        List<SubPath> subPaths = new ArrayList<>();

        for (JsonNode subPathNode : subPathsNode) {
            SubPath subPath = extractSubPath(subPathNode);
            if (isNotNull(subPath)) {
                subPaths.add(subPath);
            }
        }

        return new TransitInfo(totalTime, totalDistance, subPaths);
    }

    private static void validateOdsayErrorResponse(OdsayResponse response) {
        if (response.code().isEmpty()) {
            return;
        }

        String errorCode = response.code().get();
        if (isOdsyServerError(errorCode)) {
            log.error("ODsay 500 error: {}", response);
            // TODO: Exception 핸들링 코드 완성 이후 커스텀 에러로 변경
            throw new RuntimeException("server error");
        }

        throw new RuntimeException(response.message().orElse("경로를 찾을 수 없습니다."));
    }

    private static boolean isOdsyServerError(String errorCode) {
        return errorCode.equals(ODSAY_SERVER_ERROR);
    }

    private static JsonNode extractPath(JsonNode result) {
        return result.get("path").get(0);
    }

    private static double extractTotalDistance(JsonNode path) {
        JsonNode infoNode = path.get("info");

        return infoNode.get("totalDistance").asDouble();
    }

    private static int extractTotalTime(JsonNode path) {
        JsonNode infoNode = path.get("info");

        return infoNode.get("totalTime").asInt();
    }

    private static JsonNode extractSubPathsNode(JsonNode path) {
        return path.get("subPath");
    }

    private static SubPath extractSubPath(JsonNode subPathNode) {
        double distance = subPathNode.get("distance").asDouble();
        int sectionTime = subPathNode.get("sectionTime").asInt();

        int trafficType = subPathNode.get("trafficType").asInt();
        return switch (trafficType) {
            case 1 -> createSubwayPath(distance, sectionTime, subPathNode);
            case 2 -> createBusPath(distance, sectionTime, subPathNode);
            case 3 -> createWalkPath(distance, sectionTime);
            default -> null;
        };
    }

    private static boolean isNotNull(SubPath subPath) {
        return subPath != null;
    }

    private static SubPath createSubwayPath(double distance, int sectionTime, JsonNode subPathNode) {
        String startName = subPathNode.get("startName").asText();

        String endName = subPathNode.get("endName").asText();

        double startX = subPathNode.get("startX").asDouble();
        double startY = subPathNode.get("startY").asDouble();

        double endX = subPathNode.get("endX").asDouble();
        double endY = subPathNode.get("endY").asDouble();

        String subwayLineName = subPathNode.get("lane").get(0).get("name").asText();

        return new SubwayPath(distance, sectionTime, subwayLineName, startName, startX, startY, endName, endX, endY);
    }

    private static SubPath createBusPath(double distance, int sectionTime, JsonNode subPathNode) {
        String startName = subPathNode.get("startName").asText();

        String endName = subPathNode.get("endName").asText();

        double startX = subPathNode.get("startX").asDouble();
        double startY = subPathNode.get("startY").asDouble();

        double endX = subPathNode.get("endX").asDouble();
        double endY = subPathNode.get("endY").asDouble();

        String busNo = subPathNode.get("lane").get(0).get("busNo").asText();
        int busType = subPathNode.get("lane").get(0).get("type").asInt();

        return new BusPath(distance, sectionTime, busNo, busType, startName, startX, startY, endName, endX, endY);
    }

    private static SubPath createWalkPath(double distance, int sectionTime) {
        return new WalkPath(distance, sectionTime);
    }
}
