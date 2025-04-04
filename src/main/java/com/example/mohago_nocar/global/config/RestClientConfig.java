package com.example.mohago_nocar.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(10));
        clientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .build();
    }
}
