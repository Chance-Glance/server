package com.example.mohago_nocar.transit.infrastructure.scheduler;

import com.example.mohago_nocar.transit.application.service.DailyResetService;
import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiLimitProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class DailyLimitResetScheduler {

    private final DailyResetService dailyResetService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyLimits(){
        dailyResetService.setDailyBatchLimit(ODsayApiLimitProvider.DEFAULT_BATCH_LIMIT_PER_DAY);
        dailyResetService.resetDailyApiCount(0);
    }

}
