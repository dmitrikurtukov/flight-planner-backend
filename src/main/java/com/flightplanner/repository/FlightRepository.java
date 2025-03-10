package com.flightplanner.repository;

import com.flightplanner.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> { }
