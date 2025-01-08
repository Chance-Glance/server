package com.example.mohago_nocar.global.config;

import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponse;
import com.example.mohago_nocar.transit.infrastructure.odsay.deserializer.ODsayApiResponseDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10)) // 응답 타임아웃
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(10)) // 읽기 타임아웃
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(10))); // 쓰기 타임아웃

        return WebClient.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper())))
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(ODsayApiSuccessResponse.class, new ODsayApiResponseDeserializer()));
        return objectMapper;
    }

}
