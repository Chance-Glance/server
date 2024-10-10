package com.example.mohago_nocar.transit.application.mapper;

import com.example.mohago_nocar.transit.domain.model.*;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.RouteResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class TransitMapper {

    public static TransitInfo mapRouteResponseDtoToTransitInfo(RouteResponseDto routeResponseDto) {
        JsonNode path = extractPath(routeResponseDto);

        double totalDistance = extractTotalDistance(path);
        int totalTime = extractTotalTime(path);
        List<SubPath> subPaths = extractSubPaths(path);

        return new TransitInfo(totalTime, totalDistance, subPaths);
    }

    private static JsonNode extractPath(RouteResponseDto routeResponseDto) {
        return routeResponseDto.result().get("path").get(0);
    }

    private static double extractTotalDistance(JsonNode path) {
        JsonNode infoNode = path.get("info");

        return infoNode.get("totalDistance").asDouble();
    }

    private static int extractTotalTime(JsonNode path) {
        JsonNode infoNode = path.get("info");

        return infoNode.get("totalTime").asInt();
    }

    private static List<SubPath> extractSubPaths(JsonNode path) {
        JsonNode subPathNode = path.get("subPath");
        return convertPathesNodeToSubPaths(subPathNode);
    }

    private static List<SubPath> convertPathesNodeToSubPaths(JsonNode subPathsNode) {
        return streamJsonNodeOrEmpty(subPathsNode)
                .map(TransitMapper::convertPathNodeToSubPath)
                .toList();
    }

    private static SubPath convertPathNodeToSubPath(JsonNode subPathNode) {
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

    private static SubPath createSubwayPath(double distance, int sectionTime, JsonNode subPathNode) {
        String startSubwayStationName = subPathNode.get("startName").asText();
        String endSubWayStationName = subPathNode.get("endName").asText();

        double startSubwayStationLongitude = subPathNode.get("startX").asDouble();
        double startSubwayStationLatitude = subPathNode.get("startY").asDouble();

        double endSubwayStationLongitude = subPathNode.get("endX").asDouble();
        double endSubwayStationLatitude = subPathNode.get("endY").asDouble();

        String subwayLineName = subPathNode.get("lane").get(0).get("name").asText();

        return new SubwayPath(
                distance,
                sectionTime,
                subwayLineName,
                startSubwayStationName,
                startSubwayStationLongitude,
                startSubwayStationLatitude,
                endSubWayStationName,
                endSubwayStationLongitude,
                endSubwayStationLatitude
        );
    }

    private static SubPath createBusPath(double distance, int sectionTime, JsonNode subPathNode) {
        String startBusStopName = subPathNode.get("startName").asText();
        String endBusStopName = subPathNode.get("endName").asText();

        double startBusStopLongitude = subPathNode.get("startX").asDouble();
        double startBusStopLatitude = subPathNode.get("startY").asDouble();

        double endBusStopLongitude = subPathNode.get("endX").asDouble();
        double endBusStopLatitude = subPathNode.get("endY").asDouble();

        String busNo = subPathNode.get("lane").get(0).get("busNo").asText();
        int busType = subPathNode.get("lane").get(0).get("type").asInt();

        return new BusPath(
                distance,
                sectionTime,
                busNo,
                busType,
                startBusStopName,
                startBusStopLongitude,
                startBusStopLatitude,
                endBusStopName,
                endBusStopLongitude,
                endBusStopLatitude
        );
    }

    private static SubPath createWalkPath(double distance, int sectionTime) {
        return new WalkPath(distance, sectionTime);
    }

    private static Stream<JsonNode> streamJsonNodeOrEmpty(JsonNode node) {
        if (node == null || !node.isArray()) {
            return Stream.empty();
        }
        return StreamSupport.stream(node.spliterator(), false);
    }
}
