package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalJpaRepository extends JpaRepository<Festival, Long> {
}
