package com.example.mohago_nocar.place.infrastructure.externalApi;

import com.example.mohago_nocar.place.infrastructure.externalApi.dto.GoogleNearByPlaceResponse;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleApiClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;

    public GoogleApiClient(
            RestClient restClient,
            @Value("${google.api-key}") String apiKey,
            @Value("${google.url}") String baseUrl) {
        this.restClient = restClient;
        this.apiKey =  apiKey;
        this.baseUrl = baseUrl;
    }

    public GoogleNearByPlaceResponse request(double festivalX, double festivalY, int radius) {
        URI requestURI = buildRequestURI(festivalX, festivalY, radius);
        System.out.println(requestURI);
        return fetchGoogleNearByPlaceResponse(requestURI);
    }

    private URI buildRequestURI(double festivalX, double festivalY, int radius) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("location", festivalX + "%2C" + festivalY)
                .queryParam("radius", radius)
                .queryParam("language", "ko")
                .queryParam("type", "restaurant")
                .queryParam("key", apiKey)
                .build(true)
                .toUri();
    }

    private GoogleNearByPlaceResponse fetchGoogleNearByPlaceResponse(URI requestURI) {
        GoogleNearByPlaceResponse response = restClient.get()
                .uri(requestURI)
                .retrieve()
                .body(GoogleNearByPlaceResponse.class);

        System.out.println(restClient.get()
                .uri(requestURI)
                .retrieve()
                .body(GoogleNearByPlaceResponse.class));

        return Objects.requireNonNullElseGet(response, () -> {
            //커스텀 예외 처리
            throw new RuntimeException("축제 주변 정보 조회 실패");
        });
    }

}
