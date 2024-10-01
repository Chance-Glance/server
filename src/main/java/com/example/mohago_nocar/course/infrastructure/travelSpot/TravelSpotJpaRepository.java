package com.example.mohago_nocar.course.infrastructure.travelSpot;

import com.example.mohago_nocar.course.domain.model.travelSpot.TravelSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelSpotJpaRepository extends JpaRepository<TravelSpot, Long> {
}
