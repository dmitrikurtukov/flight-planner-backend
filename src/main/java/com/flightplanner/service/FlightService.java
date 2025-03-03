package com.flightplanner.service;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.entity.FlightEntity;
import com.flightplanner.exceptions.FlightNotFoundException;
import com.flightplanner.mappers.FlightMapper;
import com.flightplanner.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public List<FlightDto> getFilteredFlights(String destination, String departureDate, Integer minDuration, Integer maxDuration, Double minPrice, Double maxPrice) {
        List<FlightEntity> flights = flightRepository.findAll();

        return flights.stream()
                .filter(flight -> destination == null || flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> departureDate == null || flight.getDepartureTime().toLocalDate().toString().equals(departureDate))
                .filter(flight -> minDuration == null || flight.getDuration() >= minDuration)
                .filter(flight -> maxDuration == null || flight.getDuration() <= maxDuration)
                .filter(flight -> minPrice == null || flight.getPrice().doubleValue() >= minPrice)
                .filter(flight -> maxPrice == null || flight.getPrice().doubleValue() <= maxPrice)
                .map(flightMapper::toDto)
                .toList();
    }

    public FlightDto getFlightById(Long id) {
        FlightEntity flight = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
        return flightMapper.toDto(flight);
    }

    public void deleteFlight(Long id) {
        FlightEntity flightEntity = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
        flightRepository.delete(flightEntity);
    }
}
