package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayBadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import static com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode.ODSAY_SERVER_ERROR;

@Component
public class ODsayApiUriGenerator {

    private final String apiKey;
    private final String baseUrl;

    public ODsayApiUriGenerator(
            @Value("${odsay.api-key}") String apiKey,
            @Value("${odsay.url}") String baseUrl
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public URI buildRequestURI(double startX, double startY, double endX, double endY) {
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
            throw new OdsayBadRequestException(e.getMessage(), ODSAY_SERVER_ERROR);
        }
    }

}
