package com.example.mohago_nocar.transit.infrastructure.messaging.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdsayApiRequestEventRepository extends JpaRepository<OdsayApiRequestEvent, Long> {

}
