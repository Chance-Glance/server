package com.example.mohago_nocar.transit.infrastructure.odsay.client;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayCounterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class ODsayApiLimitProvider {

    public static final int DEFAULT_API_LIMIT_PER_DAY = 1000;
    public static final int DEFAULT_BATCH_LIMIT_PER_DAY = 1000;

    public static final String DAILY_BATCH_LIMIT_KEY = "odsay:batch:limit:";
    public static final String DAILY_API_COUNTER_KEY = "odsay:daily:counter:";

    private final RedisTemplate<String, Integer> redisTemplate;

    public int getDailyBatchLimit() {
        Integer limit = getBatchLimit();

        return limit != null ? limit : DEFAULT_BATCH_LIMIT_PER_DAY;
    }

    public String generateBatchKey() {
        return DAILY_BATCH_LIMIT_KEY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public void incrementRequestApiCount() {
        redisTemplate.opsForValue().increment(generateApiCounterKey());
    }

    public boolean isApiLimitExceeded() {
        Integer count = getDailyApiCounterValue();

        if (count == null) {
            log.warn("ODsay API 호출 카운트를 불러오는데 실패했습니다.");
            throw new OdsayCounterException();
        }

        return count >= DEFAULT_API_LIMIT_PER_DAY;
    }

    public String generateApiCounterKey() {
        return DAILY_API_COUNTER_KEY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public void setDailyApiCounterValue(Integer value, Duration ttl) {
        String key = generateApiCounterKey();
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void setDailyBatchLimit(Integer value, Duration ttl) {
        String key = generateBatchKey();
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Integer getDailyApiCounterValue() {
        String key = generateApiCounterKey();
        return redisTemplate.opsForValue().get(key);
    }

    private Integer getBatchLimit() {
        String key = generateBatchKey();
        return redisTemplate.opsForValue().get(key);
    }

}
