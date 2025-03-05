package com.flightplanner.service;

import com.flightplanner.dto.SeatDto;
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

    public List<SeatDto> getRecommendedSeatsForFlight(Long flightId, Integer passengerCount, Boolean windowPreferred, Boolean extraLegroom, Boolean nearExit, Boolean seatsTogether, String seatClass) {
        List<SeatEntity> availableSeats = seatRepository.findByFlightId(flightId).stream()
                .filter(seat -> !seat.getIsReserved())
                .toList();

        List<SeatEntity> filteredSeats = SeatFilterHelper.filterSeats(availableSeats, windowPreferred, extraLegroom, nearExit, passengerCount, seatsTogether, seatClass);

        return seatMapper.toDtoList(filteredSeats);
    }
}
