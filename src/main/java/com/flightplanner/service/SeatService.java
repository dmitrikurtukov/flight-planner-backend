package com.flightplanner.service;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.dto.SeatFilterCriteria;
import com.flightplanner.entity.SeatEntity;
import com.flightplanner.mappers.SeatMapper;
import com.flightplanner.repository.SeatRepository;
import com.flightplanner.utils.SeatFilterHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    public List<SeatDto> getRecommendedSeats(Long flightId, SeatFilterCriteria criteria) {
        List<SeatEntity> availableSeats = seatRepository.findByFlightId(flightId).stream().filter(seat -> !seat.getIsReserved()).toList();
        List<SeatEntity> filteredSeats = SeatFilterHelper.filterSeats(availableSeats, criteria);
        return seatMapper.toDtoList(filteredSeats);
    }

    public List<SeatDto> getAllSeats(Long flightId) {
        List<SeatEntity> allSeats = seatRepository.findByFlightId(flightId);
        return seatMapper.toDtoList(allSeats);
    }
}
