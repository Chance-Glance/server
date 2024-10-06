package com.example.mohago_nocar.place.infrastructure.externalApi.mapper;

import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.GoogleNearbyPlaceResponse;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PlaceMapper {

    public static List<PlaceResponseDto> mapGoogleNearPlaceResponseToPlaceResponseDtos(GoogleNearbyPlaceResponse googleNearbyPlaceResponse) {
        JsonNode placesNode = googleNearbyPlaceResponse.places();

        return mapPlacesNodeToResponseDtos(placesNode);
    }

    private static List<PlaceResponseDto> mapPlacesNodeToResponseDtos(JsonNode placesNode) {

        return streamJsonNodeOrEmpty(placesNode)
                .map(PlaceMapper::mapPlaceNodeToDto)
                .toList();
    }

    private static PlaceResponseDto mapPlaceNodeToDto(JsonNode place) {
        String id = place.get("id").asText();
        String address = place.get("formattedAddress").asText();

        // location
        JsonNode locationNode = place.get("location");
        Double latitude = locationNode.get("latitude").asDouble();
        Double longitude = locationNode.get("longitude").asDouble();

        // place name
        String placeName = place.get("displayName").get("text").asText();

        // place type (nullable)
        String placeType = place.has("primaryTypeDisplayName") && !place.get("primaryTypeDisplayName").isNull()
                ? place.get("primaryTypeDisplayName").get("text").asText() : "unknown";

        // rating (nullable)
        Double rating = place.has("rating") && !place.get("rating").isNull()
                ? place.get("rating").asDouble() : (double) 0;

        // userRatingCount (nullable)
        Integer userRatingCount = place.has("userRatingCount") && !place.get("userRatingCount").isNull()
                ? place.get("userRatingCount").asInt() : 0;

        // photos (nullable)
        JsonNode photoNodes = place.has("photos") && !place.get("photos").isNull()
                ? place.get("photos") : null;
        List<String> photoNames = extractPhotoName(photoNodes);

        // summary (nullable)
        String editorialSummary = place.has("editorialSummary") && !place.get("editorialSummary").isNull()
                ? place.get("editorialSummary").get("text").asText() : null;

        String generativeSummary = place.has("generativeSummary") && !place.get("generativeSummary").isNull()
                ? place.get("generativeSummary").get("description").get("text").asText() : null;

        // schedule (nullable)
        JsonNode scheduleNode = place.has("regularOpeningHours") && !place.get("regularOpeningHours").isNull()
                ? place.get("regularOpeningHours").get("weekdayDescriptions") : null;
        List<String> schedule = extractSchedule(scheduleNode);

        return PlaceResponseDto.of(
                id,
                address,
                latitude,
                longitude,
                rating,
                userRatingCount,
                placeName,
                placeType,
                photoNames,
                editorialSummary,
                generativeSummary,
                schedule
        );
    }

    private static List<String> extractPhotoName(JsonNode photoNodes) {
        return streamJsonNodeOrEmpty(photoNodes)
                .map(photo -> photo.get("name").asText())
                .toList();
    }

    private static List<String> extractSchedule(JsonNode scheduleNode) {
        return streamJsonNodeOrEmpty(scheduleNode)
                .map(JsonNode::asText)
                .toList();
    }

    private static Stream<JsonNode> streamJsonNodeOrEmpty(JsonNode node) {
        if (node == null || !node.isArray()) {
            return Stream.empty();
        }
        return StreamSupport.stream(node.spliterator(), false);
    }
}
