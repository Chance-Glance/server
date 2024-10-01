package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.repository.FestivalRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FestivalRepositoryImpl implements FestivalRepository {

    private FestivalJpaRepository festivalJpaRepository;
}
