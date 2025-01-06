package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ODsayApiLimitProviderTest {

    @Autowired
    private ODsayApiLimitProvider odsayApiLimitProvider;

    @DisplayName("API 호출 카운트를 증가시키면 업데이트된 값이 조회된다")
    @Test
    public void incrementApiCounter(){
        //given
        odsayApiLimitProvider.setDailyApiCounterValue(0, Duration.ofHours(1));

        //when
        odsayApiLimitProvider.incrementRequestApiCount();

        //then
        assertThat(odsayApiLimitProvider.getDailyApiCounterValue()).isEqualTo(1);
    }

}