package com.example.mohago_nocar.transit.infrastructure.externalApi.odsay;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayServerException;
import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayTooManyRequestsException;
import com.example.mohago_nocar.transit.infrastructure.externalApi.odsay.dto.response.OdsayRouteResponse;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.*;

import static com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode.ODSAY_SERVER_ERROR;

/**
 * ODsay API 클라이언트: 대중교통 경로 검색 요청을 처리하고 결과를 반환합니다.
 * <li> 요청 빈도를 제어하기 위해 세마포어와 스케줄러 사용 </li>
 * <li> 재시도를 위해 RetryTemplate 활용 </li>
 */
@Component
@Slf4j
public class ODsayApiClient {

    private static final int MAX_ATTEMPTS = 20;
    private static final int MIN_INTERVAL_MS = 170;
    private static final int PERMIT_THREAD_SIZE = 1;
    private static final boolean ENABLE_FIFO_ORDERING = true;

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;
    private final RetryTemplate retryTemplate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Semaphore semaphore;

    public ODsayApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${odsay.api-key}") String apiKey,
            @Value("${odsay.url}") String baseUrl) {
        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.retryTemplate = createRetryTemplate();
        this.semaphore = new Semaphore(PERMIT_THREAD_SIZE, ENABLE_FIFO_ORDERING);
    }

    public OdsayRouteResponse searchTransitRoute(double startX, double startY, double endX, double endY) {
        URI requestURI = buildRequestURI(startX, startY, endX, endY);
        return executeWithRetry(requestURI);
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
            throw new OdsayServerException(e.getMessage(), ODSAY_SERVER_ERROR);
        }
    }

    private OdsayRouteResponse executeWithRetry(URI requestURI) {

        try {
            return retryTemplate.execute(retryContext -> callTransitRouteAPI(requestURI));
        } catch (InterruptedException e) {
            log.error("ODsay API 호출 중 인터럽트 발생: {}. 요청 URI: {}", e.getMessage(), requestURI);
            throw new RuntimeException(e.getMessage());
        }
    }

    private OdsayRouteResponse callTransitRouteAPI(URI requestURI) throws InterruptedException {
        semaphore.acquire();

        try {
            return restClient.get()
                    .uri(requestURI)
                    .retrieve()
                    .body(OdsayRouteResponse.class);
        } finally {
            scheduler.schedule(() -> semaphore.release(), MIN_INTERVAL_MS, TimeUnit.MILLISECONDS);
        }
    }

    private RetryTemplate createRetryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(MAX_ATTEMPTS)
                .noBackoff()
                .retryOn(OdsayTooManyRequestsException.class)
                .build();
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
        }
    }

}
