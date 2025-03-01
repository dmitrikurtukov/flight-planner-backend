package com.flightplanner.repository;

import com.flightplanner.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByPassengerId(Long passengerId);
}
