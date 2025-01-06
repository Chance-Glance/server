package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import com.example.mohago_nocar.transit.application.service.DailyResetService;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.repository.TransitRouteJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
class ODsayApiClientTest {

    @SpyBean
    private RabbitTemplate rabbitTemplate;

    @SpyBean
    private ODsayApiLimitProvider odsayApiLimitProvider;

    @Autowired
    private ODsayApiClient odsayApiClient;

    @Autowired
    private ODsayApiUriGenerator odsayApiUriGenerator;

    @Autowired
    private TransitRouteJpaRepository transitRouteRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(rabbitTemplate);
    }

    @AfterEach
    void tearDown() {
        transitRouteRepository.deleteAllInBatch();
    }

    @DisplayName("API 요청 제한 초과 시 메시지는 지연 큐로 전송된다.")
    @Test
    public void requestOdsayApiWithExceededApiCallLimit() throws InterruptedException {
        //given
        OdsayApiRequestEvent event = createEvent();
        mockDeferredQueueProducerBehavior();
        doReturn(true).when(odsayApiLimitProvider).isApiLimitExceeded();

        //when
        odsayApiClient.requestOdsayApi(event);

        //then
        verify(rabbitTemplate, times(1))
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.deferred.key"),
                        any(OdsayApiRequestEvent.class)
                );
        verify(odsayApiLimitProvider, never()).incrementRequestApiCount();
    }

/*
    // ODsay API 호출 횟수를 소모하는 테스트이므로 주석 처리합니다.

    @DisplayName("메시지큐에 메시지가 전송되면 메시지 소비자가 속도 제한을 준수하며 API를 호출한다.")
    @Test
    public void requestOdsayApi() throws InterruptedException {
        //given
        dailyResetService.resetDailyApiCount(0);
        OdsayApiRequestEvent event = createEvent();
        int totalRequests = 30;

        //when
        for (int i = 0; i < totalRequests; i++) {
            rabbitTemplate.convertAndSend("odsay.direct.exchange", "odsay.priority.key", event);
        }

        Thread.sleep(7000);

        //then
        assertThat(odsayApiLimitProvider.getDailyApiCounterValue()).isEqualTo(totalRequests);
        assertThat(transitRouteRepository.findAll()).hasSize(totalRequests);
    }
*/

    private OdsayApiRequestEvent createEvent() {
        RoutePoint departure = RoutePoint.from("경기대학교", 127.035833, 37.30048499999999);
        RoutePoint arrival = RoutePoint.from("수원역", 126.9994077, 37.2664398);

        URI requestURI = odsayApiUriGenerator.buildRequestURI(departure.getLongitude(), departure.getLatitude(), arrival.getLongitude(), arrival.getLatitude());
        return OdsayApiRequestEvent.of(requestURI, departure, arrival);
    }

    private void mockDeferredQueueProducerBehavior() {
        doNothing().when(rabbitTemplate)
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.deferred.key"),
                        any(OdsayApiRequestEvent.class)
                );
    }

}