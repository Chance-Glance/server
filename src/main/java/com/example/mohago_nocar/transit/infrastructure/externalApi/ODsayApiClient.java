package com.example.mohago_nocar.transit.infrastructure.externalApi;

import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.OdsaySearchRouteResponseDto;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.RouteResponseDto;
import com.example.mohago_nocar.transit.infrastructure.mapper.OdsayRouteMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Objects;

@Component
public class ODsayApiClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;

    public ODsayApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${odsay.api-key}") String apiKey,
            @Value("${odsay.url}") String baseUrl) {
        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public RouteResponseDto searchRoute(double startX, double startY, double endX, double endY) {
        URI requestURI = buildRequestURI(startX, startY, endX, endY);
        OdsaySearchRouteResponseDto odsayRouteResponse = fetchOdsayRouteResponse(requestURI);

        return OdsayRouteMapper.mapOdsayRouteResponseToRouteResponse(odsayRouteResponse);
    }

    private URI buildRequestURI(double startX, double startY, double endX, double endY) {
        String encodedApiKey = createEncodedApiKey();

        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("SX", startX)
                .queryParam("SY", startY)
                .queryParam("EX", endX)
                .queryParam("EY", endY)
                .queryParam("apiKey", encodedApiKey)
                .build(true)
                .toUri();
    }

    private String createEncodedApiKey() {
        try {
            return URLEncoder.encode(apiKey, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            // TODO: Exception 핸들링 코드 완성 이후 커스텀 에러로 변경
            throw new RuntimeException(e);
        }
    }

    private OdsaySearchRouteResponseDto fetchOdsayRouteResponse(URI requestURI) {
        OdsaySearchRouteResponseDto response = restClient.get()
                .uri(requestURI)
                .retrieve()
                .body(OdsaySearchRouteResponseDto.class);

        return Objects.requireNonNullElseGet(response, () -> {
            // TODO: Exception 핸들링 코드 완성 이후 커스텀 에러로 변경
            throw new RuntimeException("서버 에러");
        });
    }
}
