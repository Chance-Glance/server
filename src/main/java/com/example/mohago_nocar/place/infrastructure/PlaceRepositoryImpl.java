package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.global.util.ObjectMapperUtil;
import com.example.mohago_nocar.place.domain.model.Place;
import com.example.mohago_nocar.place.domain.repository.PlaceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepository {

    private static final String KEY_PREFIX = "festival:places:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public List<Place> findByIds(Long festivalId, List<String> placeIds) {
        List<Place> places = getFestivalAroundPlaces(festivalId);

        return places.stream()
                .filter(place -> placeIds.contains(place.getId()))
                .toList();
    }

    @Override
    public List<Place> getFestivalAroundPlaces(Long festivalId) {
        String placesInJson = readCache(generateCacheKey(festivalId));

        if (StringUtils.isEmpty(placesInJson)) {
            return Collections.emptyList();
        }

        return objectMapperUtil.readValue(placesInJson, new TypeReference<>() {
        });
    }

    @Override
    public List<Place> saveAllToCache(Long festivalId, List<Place> toSavePlaces) {
        String key = generateCacheKey(festivalId);
        saveToCache(key, toSavePlaces);
        return readFromSavedCache(key);
    }

    private void saveToCache(String key, List<Place> places) {
        String placesJson = objectMapperUtil.writeValue(places);
        redisTemplate.opsForValue().set(key, placesJson, 2, TimeUnit.HOURS);
    }

    private List<Place> readFromSavedCache(String key) {
        return objectMapperUtil.readValue(readCache(key), new TypeReference<>() {
        });
    }

    private String readCache(String redisKey) {
        return redisTemplate.opsForValue().get(redisKey);
    }

    private String generateCacheKey(Long festivalId) {
        return KEY_PREFIX + festivalId;
    }

}
