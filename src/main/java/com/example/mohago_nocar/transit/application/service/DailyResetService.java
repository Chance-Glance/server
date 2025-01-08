package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiLimitProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyResetService {

    private final ODsayApiLimitProvider odsayApiLimitProvider;

    public void setDailyBatchLimit(int limit) {
        Duration ttl = calculateTtlUntilMidnight();
        odsayApiLimitProvider.setDailyBatchLimit(limit, ttl);
        log.info("Set daily batch limit: limit={}, ttl={}", limit, ttl);
    }

    public void resetDailyApiCount(int count) {
        Duration ttl = calculateTtlUntilMidnight();
        odsayApiLimitProvider.setDailyApiCounterValue(count, ttl);
        log.info("reset daily API count: limit={}, ttl={}", count, ttl);
    }

    private Duration calculateTtlUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, midnight); // 현재 시각부터 자정까지의 시간 계산
    }

}
