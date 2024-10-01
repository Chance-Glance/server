package com.example.mohago_nocar.course.infrastructure.travelSpot;

import com.example.mohago_nocar.course.domain.repository.TravelSpotRepository;
import com.example.mohago_nocar.course.infrastructure.travelSpot.TravelSpotJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TravelSpotRepositoryImpl implements TravelSpotRepository {

    private TravelSpotJpaRepository travelSpotJpaRepository;
}
