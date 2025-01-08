package com.example.mohago_nocar.transit.application.mapper;

import com.example.mohago_nocar.transit.domain.model.*;
import com.example.mohago_nocar.transit.domain.model.segment.BusSegment;
import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.model.segment.SubwaySegment;
import com.example.mohago_nocar.transit.domain.model.segment.WalkingSegment;
import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class TransitMapper {

    private static final int SUBWAY = 1;
    private static final int BUS = 2;
    private static final int WALKING = 3;
    private static final int EARTH_RADIUS = 6371;
    private static final double METER_TO_KILOMETER = 0.001;

    public static TransitRouteWithSegments mapOdsayApiResponseToTransitRouteWithSegments(
            ODsayApiSuccessResponse apiResponse,
            RoutePoint departure,
            RoutePoint arrival
    ) {
        if (apiResponse.isTooShortDistance()) {
            return createWalkingRoute(departure, arrival);
        }

        JsonNode path = extractPath(apiResponse);
        List<RouteSegment> segments = extractRouteSegments(path);

        double totalDistance = extractTotalDistance(path);
        int totalTime = extractTotalTime(path);
        TransitRoute transitRoute = TransitRoute.from(departure, arrival, totalTime, totalDistance);

        return TransitRouteWithSegments.from(transitRoute, segments);
    }

    private static TransitRouteWithSegments createWalkingRoute(RoutePoint departure, RoutePoint arrival) {
        double walkingDistance = getKmDist(departure, arrival);
        int walkingTime = (int) Math.round(walkingDistance * 15);

        TransitRoute transitRoute = TransitRoute.from(departure, arrival, walkingTime, walkingDistance);
        WalkingSegment walkingSegment = WalkingSegment.from(walkingDistance, walkingTime, 0);

        return TransitRouteWithSegments.from(transitRoute, List.of(walkingSegment));
    }

    /**
     * 두 위치(Location) 사이의 거리를 킬로미터 단위로 계산합니다.
     * @param departure
     * @param arrival
     * @return 두 위치(Location) 사이의 거리
     */
    private static Double getKmDist(RoutePoint departure, RoutePoint arrival) {
        Double dx = Math.abs(departure.getLongitude() - arrival.getLongitude());
        dx = Math.min(dx, 360 - dx);

        Double dy = Math.abs(departure.getLatitude() - arrival.getLatitude());

        Double longitudeDist = convertLongitudeToKmDist(dx, departure.getLatitude());
        Double latitudeDist = convertLatitudeToKmDist(dy);

        return Math.sqrt(longitudeDist * longitudeDist + latitudeDist * latitudeDist);
    }

    private static Double convertLongitudeToKmDist(Double dx, Double stdLatitude) {

        return EARTH_RADIUS * dx * Math.cos(stdLatitude) * Math.PI / 180;
    }

    private static Double convertLatitudeToKmDist(Double dy) {

        return EARTH_RADIUS * dy * Math.PI / 180;
    }

    private static JsonNode extractPath(ODsayApiSuccessResponse odsayApiResponse) {
        return odsayApiResponse.result().get("path").get(0);
    }

    private static double extractTotalDistance(JsonNode path) {
        JsonNode infoNode = path.get("info");
        double distanceMeters = infoNode.get("totalDistance").asDouble();

        return distanceMeters * METER_TO_KILOMETER;
    }

    private static int extractTotalTime(JsonNode path) {
        JsonNode infoNode = path.get("info");

        return infoNode.get("totalTime").asInt();
    }

    private static List<RouteSegment> extractRouteSegments(JsonNode path) {
        JsonNode segmentsNode = path.get("subPath");
        return convertToRouteSegments(segmentsNode);
    }

    private static List<RouteSegment> convertToRouteSegments(JsonNode segmentsNode) {
        JsonNode[] segmentNodeArray = streamJsonNodeOrEmpty(segmentsNode).toArray(JsonNode[]::new);

        return IntStream.range(0, segmentNodeArray.length)
                .mapToObj(index -> convertToRouteSegment(segmentNodeArray[index], index))
                .toList();
    }

    private static RouteSegment convertToRouteSegment(JsonNode segmentNode, int index) {
        double distance = (segmentNode.get("distance").asDouble()) * METER_TO_KILOMETER;
        int sectionTime = segmentNode.get("sectionTime").asInt();
        int trafficType = segmentNode.get("trafficType").asInt();

        return switch (trafficType) {
            case SUBWAY -> createSubwaySegment(distance, sectionTime, segmentNode, index);
            case BUS -> createBusSegment(distance, sectionTime, segmentNode, index);
            case WALKING -> createWalkingSegment(distance, sectionTime, index);
            default -> null; // todo 로깅 + 에러
        };
    }

    private static RouteSegment createSubwaySegment(double distance, int sectionTime, JsonNode segmentNode, int sequence) {
        String subwayLineName = segmentNode.get("lane").get(0).get("name").asText();

        RoutePoint departure = getDeparture(segmentNode);
        RoutePoint arrival = getArrival(segmentNode);

        return SubwaySegment.from(distance, sectionTime, subwayLineName, departure, arrival, sequence);
    }

    private static RouteSegment createBusSegment(double distance, int sectionTime, JsonNode segmentNode, int sequence) {
        String busNo = segmentNode.get("lane").get(0).get("busNo").asText();
        int busType = segmentNode.get("lane").get(0).get("type").asInt();

        RoutePoint departure = getDeparture(segmentNode);
        RoutePoint arrival = getArrival(segmentNode);

        return BusSegment.from(distance, sectionTime, busNo, busType, departure, arrival, sequence);
    }

    private static RouteSegment createWalkingSegment(double distance, int sectionTime, int sequence) {
        return WalkingSegment.from(distance, sectionTime, sequence);
    }

    private static RoutePoint getDeparture(JsonNode subPathNode) {
        String name = subPathNode.get("startName").asText();
        double longitude = subPathNode.get("startX").asDouble();
        double latitude = subPathNode.get("startY").asDouble();

        return RoutePoint.from(name, longitude, latitude);
    }

    private static RoutePoint getArrival(JsonNode subPathNode) {
        String name = subPathNode.get("endName").asText();
        double longitude = subPathNode.get("endX").asDouble();
        double latitude = subPathNode.get("endY").asDouble();

        return RoutePoint.from(name, longitude, latitude);
    }

    private static Stream<JsonNode> streamJsonNodeOrEmpty(JsonNode node) {
        if (node == null || !node.isArray()) {
            return Stream.empty();
        }
        return StreamSupport.stream(node.spliterator(), false);
    }

}
