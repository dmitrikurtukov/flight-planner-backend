package com.flightplanner.repository;

import com.flightplanner.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
    PassengerEntity findByEmail(String email);
}
