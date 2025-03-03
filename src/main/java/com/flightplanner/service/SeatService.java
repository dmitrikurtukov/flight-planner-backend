package com.flightplanner.service;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.mappers.SeatMapper;
import com.flightplanner.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    public List<SeatDto> getSeatsByFlightId(Long flightId) {
        return seatMapper.toDtoList(seatRepository.findByFlightId(flightId));
    }
}
