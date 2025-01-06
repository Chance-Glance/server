package com.example.mohago_nocar.transit.infrastructure.batch.repository;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
class OdsayApiRequestEntryRepositoryTest {

    @Autowired
    private OdsayApiRequestEntryRepository entryRepository;

    @AfterEach
    void tearDown() {
        entryRepository.deleteAll();
    }

    @DisplayName("createdAt 기준으로 오래된 요청부터 지정된 개수만큼 조회한다")
    @Test
    public void findAllByOrderByCreatedAtAsc(){
        //given
        OdsayApiRequestEntry item1 = OdsayApiRequestEntry.of(URI.create("testUri1"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item2 = OdsayApiRequestEntry.of(URI.create("testUri2"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item3 = OdsayApiRequestEntry.of(URI.create("testUri3"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item4 = OdsayApiRequestEntry.of(URI.create("testUri4"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item5 = OdsayApiRequestEntry.of(URI.create("testUri5"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item6 = OdsayApiRequestEntry.of(URI.create("testUri6"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item7 = OdsayApiRequestEntry.of(URI.create("testUri7"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item8 = OdsayApiRequestEntry.of(URI.create("testUri8"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        OdsayApiRequestEntry item9 = OdsayApiRequestEntry.of(URI.create("testUri9"), RoutePoint.from("가짜 출발", 123.123, 32.22), RoutePoint.from("가짜 도착", 123.123, 32.22));
        entryRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9));

        int size = 5;
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when
        List<OdsayApiRequestEntry> results = entryRepository.findAllByOrderByCreatedAtAsc(pageRequest);

        //then
        assertThat(results).hasSize(size)
                .extracting("requestUri")
                .containsExactly(
                        item1.getRequestUri(),
                        item2.getRequestUri(),
                        item3.getRequestUri(),
                        item4.getRequestUri(),
                        item5.getRequestUri()
                );

    }

}