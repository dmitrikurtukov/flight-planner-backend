package com.flightplanner.service;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.dto.FlightFilterCriteria;
import com.flightplanner.entity.FlightEntity;
import com.flightplanner.exceptions.FlightNotFoundException;
import com.flightplanner.mappers.FlightMapper;
import com.flightplanner.repository.FlightRepository;
import com.flightplanner.utils.FlightFilterHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public List<FlightDto> getFlights(FlightFilterCriteria criteria) {
        List<FlightEntity> filteredFlights = FlightFilterHelper.filterFlights(flightRepository.findAll(), criteria);
        List<FlightEntity> sortedFlights = FlightFilterHelper.sortFlights(filteredFlights, criteria.sortBy());
        return flightMapper.toDtoList(sortedFlights);
    }

    public FlightDto getFlightById(Long id) {
        FlightEntity flight = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
        return flightMapper.toDto(flight);
    }
}
