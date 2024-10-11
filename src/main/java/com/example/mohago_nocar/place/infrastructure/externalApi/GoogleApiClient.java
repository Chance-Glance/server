package com.example.mohago_nocar.place.infrastructure.externalApi;

import com.example.mohago_nocar.place.infrastructure.externalApi.dto.request.GoogleNearPlaceRequest;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.GoogleNearbyPlaceResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.GooglePlaceImageResponse;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import com.example.mohago_nocar.place.infrastructure.externalApi.mapper.GooglePlaceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class GoogleApiClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;

    private static final int MAX_RESULT_COUNT = 20;

    private static final String RANK_PREFERENCE = "POPULARITY";

    private static final String LANGUAGE_CODE = "ko";

    private static final String FIELD_MASK = String.join(",",
            "places.displayName",
            "places.nationalPhoneNumber",
            "places.id",
            "places.formattedAddress",
            "places.rating",
            "places.userRatingCount",
            "places.location",
            "places.websiteUri",
            "places.regularOpeningHours.weekdayDescriptions",
            "places.primaryTypeDisplayName",
            "places.editorialSummary",
            "places.generativeSummary",
            "places.photos.name");

    private static final List<String> REQUEST_PLACE_TYPES = List.of(
            "art_gallery",
            "museum",
            "performing_arts_theater",
            "amusement_center",
            "amusement_park",
            "aquarium",
            "banquet_hall",
            "bowling_alley",
            "casino",
            "hiking_area",
            "historical_landmark",
            "marina",
            "movie_rental",
            "movie_theater",
            "national_park",
            "night_club",
            "park",
            "zoo",
            "american_restaurant",
            "bakery",
            "bar",
            "barbecue_restaurant",
            "breakfast_restaurant",
            "brunch_restaurant",
            "cafe",
            "chinese_restaurant",
            "coffee_shop",
            "fast_food_restaurant",
            "french_restaurant",
            "hamburger_restaurant",
            "ice_cream_shop",
            "indian_restaurant",
            "indonesian_restaurant",
            "italian_restaurant",
            "japanese_restaurant",
            "korean_restaurant",
            "mediterranean_restaurant",
            "mexican_restaurant",
            "middle_eastern_restaurant",
            "pizza_restaurant",
            "ramen_restaurant",
            "restaurant",
            "sandwich_shop",
            "seafood_restaurant",
            "steak_house",
            "sushi_restaurant",
            "spa",
            "gift_shop",
            "shopping_mall",
            "playground"
    );

    public GoogleApiClient(
            RestClient.Builder restClient,
            @Value("${google.api-key}") String apiKey,
            @Value("${google.url}") String baseUrl) {
        this.restClient = restClient.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public List<PlaceResponseDto> searchNearbyPlacesWithImageUris(double festivalX, double festivalY, int radius) {
        URI requestURI = buildNearPlaceRequestURI();
        GoogleNearbyPlaceResponse googleNearPlaceResponse = fetchNearPlaceResponse(requestURI, festivalX, festivalY, radius);
        List<PlaceResponseDto> placeResponseDtos = GooglePlaceMapper.mapGoogleNearPlaceResponseToPlaceResponseDtos(googleNearPlaceResponse);

        return convertPhotoNamesToUris(placeResponseDtos);
    }

    private URI buildNearPlaceRequestURI() {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("places:searchNearby")
                .build(true)
                .toUri();
    }

    private GoogleNearbyPlaceResponse fetchNearPlaceResponse(URI requestURI, double festivalX, double festivalY, int radius) {
        GoogleNearPlaceRequest requestBody = GoogleNearPlaceRequest.of(
                REQUEST_PLACE_TYPES, MAX_RESULT_COUNT, festivalX, festivalY, radius, RANK_PREFERENCE, LANGUAGE_CODE);

        try {
            return restClient.post()
                    .uri(requestURI)
                    .header("X-Goog-Api-Key", apiKey)
                    .header("Content-Type", "application/json")
                    .header("X-Goog-FieldMask", FIELD_MASK)
                    .body(requestBody)
                    .retrieve()
                    .body(GoogleNearbyPlaceResponse.class);

        } catch (Exception e) {
            // TODO: 커스텀 에러 변경
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Google API 요청을 통해 주어진 PlaceResponseDto  List의 photos 필드 값을 photoName에서 photoUri로 변환합니다.
     *
     * @param placeResponseDtos 변환할 PlaceResponseDto  목록 (변환 전의 photos 필드 값은 photoName)
     * @return 변환된 PlaceResponseDto  목록 (photos 필드 값은 photoUri)
     */
    private List<PlaceResponseDto> convertPhotoNamesToUris(List<PlaceResponseDto> placeResponseDtos) {
        return placeResponseDtos.stream()
                .map(this::convertPhotoNamesToUrisForDto)
                .collect(Collectors.toList());
    }

    private PlaceResponseDto convertPhotoNamesToUrisForDto(PlaceResponseDto placeResponseDto) {
        List<String> photoNames = placeResponseDto.getPhotos();
        List<String> photoUris = searchPlaceImagesFrom(photoNames);

        return placeResponseDto.withUpdatedPhotos(photoUris);
    }

    private List<String> searchPlaceImagesFrom(List<String> photoNames) {
        return photoNames.stream()
                .map(this::searchPlaceImageUri)
                .collect(Collectors.toList());
    }

    private String searchPlaceImageUri(String photoName) {
        URI uri = buildPlaceImageRequestURI(photoName);
        GooglePlaceImageResponse imageResponse = fetchPlaceImageResponse(uri);

        return imageResponse.photoUri();
    }

    private URI buildPlaceImageRequestURI(String name) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path(name)
                .path("/media")
                .queryParam("key", apiKey)
                .queryParam("maxHeightPx", 4800)
                .queryParam("skipHttpRedirect", true)
                .build(true)
                .toUri();
    }

    private GooglePlaceImageResponse fetchPlaceImageResponse(URI uri) {
        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GooglePlaceImageResponse.class);

        } catch (Exception e) {
            // TODO: 커스텀 에러 변경
            throw new RuntimeException(e.getMessage());
        }
    }
}
