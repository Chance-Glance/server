package com.example.mohago_nocar.transit.infrastructure.batch.config;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.application.service.DailyResetService;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.odsay.ODsayApiUriGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
class TransitRouteBatchConfigTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OdsayApiRequestEntryRepository requestEntryRepository;

    @Autowired
    private ODsayApiUriGenerator odsayApiUriGenerator;

    @Autowired
    private DailyResetService dailyResetService;

    @BeforeEach
    void setUp() {
        dailyResetService.setDailyBatchLimit(1000);
        dailyResetService.resetDailyApiCount(0);
        Mockito.reset(rabbitTemplate);
    }

    @AfterEach
    void tearDown() {
        requestEntryRepository.deleteAllInBatch();
    }

    @DisplayName("ODsay API 호출 요청 메시지를 우선순위 큐로 전송한다")
    @Test
    public void odsayApiJobWithNoDeferredMessages() throws Exception {
        //given
        saveNinetyOdsayApiRequestEntiry();
        mockPriorityQueueProducerBehavior();
        mockDeferredQueueConsumerBehavior();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExecutionContext().getInt("processedDeferredMessages")).isZero();

        assertThat(jobExecution.getExecutionContext().getInt("processedFreshMessages")).isEqualTo(90);
        assertThat(requestEntryRepository.findAll()).isEmpty();
        verify(rabbitTemplate, times(90))
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.priority.key"),
                        any(OdsayApiRequestEvent.class),
                        any(MessagePostProcessor.class)
                );

    }

    @DisplayName("지연된 메시지가 있다면 해당 메시지부터 우선순위 큐로 전송한다.")
    @Test
    public void odsayApiJobWithDeferredMessages() throws Exception {
        //given
        saveNinetyOdsayApiRequestEntiry();
        mockPriorityQueueProducerBehavior();
        mockDeferredQueueConsumerBehaivor(50);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        assertThat(jobExecution.getExecutionContext().getInt("processedDeferredMessages")).isEqualTo(50);
        verify(rabbitTemplate, times(51))
                .receiveAndConvert(
                        eq("odsay.deferred.queue"),
                        eq(500L),
                        any(ParameterizedTypeReference.class)
                );

        assertThat(jobExecution.getExecutionContext().getInt("processedFreshMessages")).isEqualTo(90);
        assertThat(requestEntryRepository.findAll()).isEmpty();

        verify(rabbitTemplate, times(140))
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.priority.key"),
                        any(OdsayApiRequestEvent.class),
                        any(MessagePostProcessor.class)
                );

    }

    @DisplayName("하루에 우선순위 큐로 전송 가능한 메시지는 최대 1000개이다.")
    @Test
    public void odsayApiJobWithExceededDeferredMessages() throws Exception {
        //given
        saveNinetyOdsayApiRequestEntiry();
        mockPriorityQueueProducerBehavior();
        mockDeferredQueueConsumerBehaivor(1001);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        assertThat(jobExecution.getExecutionContext().getInt("processedDeferredMessages")).isEqualTo(1000);
        verify(rabbitTemplate, times(1000)) // 1001개부터는 메시지 전송 제한 개수를 초과했으므로 receiveAndConvert 메서드를 호출하지 않음
                .receiveAndConvert(
                        eq("odsay.deferred.queue"),
                        eq(500L),
                        any(ParameterizedTypeReference.class)
                );

        assertThat(jobExecution.getExecutionContext().getInt("processedFreshMessages")).isEqualTo(0);
        assertThat(requestEntryRepository.findAll()).hasSize(90);

        verify(rabbitTemplate, times(1000))
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.priority.key"),
                        any(OdsayApiRequestEvent.class),
                        any(MessagePostProcessor.class)
                );
    }

    private void mockDeferredQueueConsumerBehaivor(int deferredMessageCounter) {
        List<OdsayApiRequestEvent> events = createEvents(deferredMessageCounter);
        AtomicInteger count = new AtomicInteger(0);
        when(rabbitTemplate.receiveAndConvert(
                eq("odsay.deferred.queue"),
                eq(500L),
                any(ParameterizedTypeReference.class)))
                .thenAnswer(invocation -> {
                    if (count.get() < deferredMessageCounter) {
                        return events.get(count.getAndIncrement());
                    }
                    return null;
                });
    }

    private List<OdsayApiRequestEvent> createEvents(int num) {
        List<OdsayApiRequestEvent> odsayApiRequestEvents = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            odsayApiRequestEvents.add(createEvent());
        }
        return odsayApiRequestEvents;
    }

    private void saveNinetyOdsayApiRequestEntiry() {
        List<OdsayApiRequestEntry> odsayApiRequestEntries = createOdsayApiRequestEntries();
        requestEntryRepository.saveAll(odsayApiRequestEntries);
    }

    private void mockDeferredQueueConsumerBehavior() {
        when(rabbitTemplate.receiveAndConvert(
                eq("odsay.deferred.queue"),
                eq(500L),
                any(ParameterizedTypeReference.class)))
                .thenReturn(null);
    }

    private void mockPriorityQueueProducerBehavior() {
        doNothing().when(rabbitTemplate)
                .convertAndSend(
                        eq("odsay.direct.exchange"),
                        eq("odsay.priority.key"),
                        any(OdsayApiRequestEvent.class),
                        any(MessagePostProcessor.class)
                );
    }

    private OdsayApiRequestEvent createEvent() {
        return OdsayApiRequestEvent.of(URI.create("test"),
                RoutePoint.from("수원화성", 127.0119379, 37.2871202),
                RoutePoint.from("화성행궁", 127.013727, 37.2819666));
    }

    private List<OdsayApiRequestEntry> createOdsayApiRequestEntries() {
        List<Location> travelLocations = new ArrayList<>();

        travelLocations.add(Location.from("수원화성", 127.0119379, 37.2871202 ));
        travelLocations.add(Location.from("화성행궁", 127.013727, 37.2819666 ));
        travelLocations.add(Location.from("팔달문", 127.0167348, 37.2775525 ));
        travelLocations.add(Location.from("수원통닭거리", 127.0177197, 37.2793262 ));
        travelLocations.add(Location.from("광교호수공원", 127.0659215, 37.2830911));
        travelLocations.add(Location.from("장안문", 127.0142055, 37.2888038));
        travelLocations.add(Location.from("경기도박물관", 127.071882, 37.276788));
        travelLocations.add(Location.from("화홍문", 127.0176056, 37.28746));
        travelLocations.add(Location.from("서호공원", 127.017505, 37.252293));
        travelLocations.add(Location.from("창룡문", 127.02514169999999, 37.287738499999996));

        List<OdsayApiRequestEntry> requests = new ArrayList<>();

        for (Location from : travelLocations) {
            for (Location to : travelLocations) {
                if (to.equals(from)) {
                    continue;
                }
                requests.add(createApiCallRequest(from, to));
            }
        }
        requests.forEach(System.out::println);

        return requests;
    }


    private OdsayApiRequestEntry createApiCallRequest(Location from, Location to) {
        URI requestURI = odsayApiUriGenerator.buildRequestURI(
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude()
        );

        return OdsayApiRequestEntry.of(requestURI, RoutePoint.parse(from), RoutePoint.parse(to));
    }

}