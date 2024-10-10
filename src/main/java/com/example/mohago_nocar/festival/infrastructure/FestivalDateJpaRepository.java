package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FestivalDateJpaRepository extends JpaRepository<Festival,Long> {
    List<Festival> findByFestivalDateBetween(LocalDate startDate, LocalDate endDate);
}
