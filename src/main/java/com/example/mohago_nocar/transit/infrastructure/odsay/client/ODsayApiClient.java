package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import com.example.mohago_nocar.transit.infrastructure.messaging.TransitRouteEventManager;
import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponseWrapper;
import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiSuccessResponse;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * ODsay API 클라이언트로, API 요청을 처리하고 결과를 캐시합니다.
 * 요청은 153ms 간격으로 처리되며, API 제한에 도달하면 지연 큐로 전송됩니다.
 */
@Component
@Slf4j
public class ODsayApiClient {

    private static final int INTERVAL_MS = 153;
    private static final int KEEP_ALIVE_TIME_SECONDS = 60;
    private static final int MAX_POOL_SIZE = Integer.MAX_VALUE;

    private final ExecutorService executorService = new ThreadPoolExecutor(
            0, MAX_POOL_SIZE, KEEP_ALIVE_TIME_SECONDS, TimeUnit.SECONDS, new SynchronousQueue<>());

    private final RestClient restClient;
    private final TransitUseCase transitUseCase;
    private final TransitRouteEventManager routeEventManager;
    private final ODsayApiLimitProvider odsayApiLimitProvider;
    private FluxSink<OdsayApiRequestEvent> eventSink;

    public ODsayApiClient(
            RestClient.Builder restClientBuilder,
            TransitUseCase transitUseCase,
            TransitRouteEventManager routeEventManager,
            ODsayApiLimitProvider odsayApiLimitProvider
    ) {
        this.restClient = restClientBuilder.build();
        this.transitUseCase = transitUseCase;
        this.routeEventManager = routeEventManager;
        this.odsayApiLimitProvider = odsayApiLimitProvider;
        initializeSingleThreadedEventProcessor();
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.odsay.priority}")
    public void requestOdsayApi(OdsayApiRequestEvent event) {
        eventSink.next(event);
    }

    public void processRequest(OdsayApiRequestEvent event) {
        if (odsayApiLimitProvider.isApiLimitExceeded()) {
            routeEventManager.sendToDeferredQueue(event);
            return;
        }

        ODsayApiSuccessResponse response = callOdsayApi(event);
        odsayApiLimitProvider.incrementRequestApiCount();
        executorService.submit(()-> cacheTransitRoute(
                ODsayApiSuccessResponseWrapper.of(response, event.getDeparture(), event.getArrival())));
    }

    private ODsayApiSuccessResponse callOdsayApi(OdsayApiRequestEvent event) {
        return restClient.get()
                .uri(event.getRequestUri())
                .retrieve()
                .body(ODsayApiSuccessResponse.class);
    }

    private void cacheTransitRoute(ODsayApiSuccessResponseWrapper response) {
        transitUseCase.saveTransitRouteWithSegments(response);
    }

    private void initializeSingleThreadedEventProcessor() {
        Flux.<OdsayApiRequestEvent>create(sink -> this.eventSink = sink)
                .publishOn(Schedulers.single())
                .delayElements(Duration.ofMillis(INTERVAL_MS), Schedulers.single())
                .doOnNext(this::processRequest)
                .onErrorContinue((throwable, event) -> {
                    log.error("Error processing event: {}", event, throwable);
                    handleOdsayApiException(throwable, (OdsayApiRequestEvent) event);
                })
                .subscribe();
    }

    private void handleOdsayApiException(Throwable throwable, OdsayApiRequestEvent event) {
        // todo: 디스코드 알림 전송
        log.info("디스코드 알림 전송");
        routeEventManager.sendToDLQ(event);
    }

}
