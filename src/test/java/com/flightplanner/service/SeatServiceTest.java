package com.flightplanner.service;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.dto.SeatFilterCriteria;
import com.flightplanner.entity.FlightEntity;
import com.flightplanner.entity.SeatEntity;
import com.flightplanner.mappers.SeatMapper;
import com.flightplanner.repository.SeatRepository;
import com.flightplanner.utils.SeatFilterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatService seatService;

    private SeatEntity seat1, seat2, seat3;

    private FlightEntity testFlight;

    @BeforeEach
    void setUp() {
        testFlight = new FlightEntity();
        testFlight.setId(1L);

        seat1 = new SeatEntity(1L, testFlight, "1A", "First Class", false);
        seat2 = new SeatEntity(2L, testFlight, "1B", "First Class", false);
        seat3 = new SeatEntity(3L, testFlight, "2A", "Economy", true);
    }

    @Test
    void getAllSeats_ShouldReturnAllSeats() {
        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(seat1, seat2, seat3));
        when(seatMapper.toDtoList(any())).thenReturn(List.of(
                new SeatDto("1A", "First Class", false),
                new SeatDto("1B", "First Class", false),
                new SeatDto("2A", "Economy", true)
        ));

        List<SeatDto> seats = seatService.getAllSeats(1L);

        assertEquals(3, seats.size());
        verify(seatRepository, times(1)).findByFlightId(1L);
        verify(seatMapper, times(1)).toDtoList(any());
    }

    @Test
    void getRecommendedSeats_ShouldFilterByWindowSeat() {
        SeatFilterCriteria criteria = new SeatFilterCriteria(null, true, null, null, null, null);
        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(seat1, seat2, seat3));
        when(seatMapper.toDtoList(any())).thenAnswer(_ -> {
            List<SeatEntity> filteredSeats = SeatFilterHelper.filterSeats(List.of(seat1, seat2, seat3), criteria);
            return filteredSeats.stream().map(s -> new SeatDto(s.getSeatNumber(), s.getSeatClass(), s.getIsReserved())).toList();
        });

        List<SeatDto> seats = seatService.getRecommendedSeats(1L, criteria);

        assertEquals(2, seats.size());
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("1A")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("2A")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("1B")));
    }

    @Test
    void getRecommendedSeats_ShouldFilterByExtraLegroom() {
        SeatFilterCriteria criteria = new SeatFilterCriteria(null, null, true, null, null, null);

        SeatEntity seatExtra1 = new SeatEntity(4L, testFlight, "1C", "Business", false);
        SeatEntity seatExtra2 = new SeatEntity(5L, testFlight, "11A", "Economy", false);
        SeatEntity normalSeat = new SeatEntity(6L, testFlight, "5B", "Economy", false);

        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(seat1, seat2, seat3, seatExtra1, seatExtra2, normalSeat));
        when(seatMapper.toDtoList(any())).thenAnswer(_ -> {
            List<SeatEntity> filteredSeats = SeatFilterHelper.filterSeats(List.of(seat1, seat2, seat3, seatExtra1, seatExtra2, normalSeat), criteria);
            return filteredSeats.stream()
                    .map(s -> new SeatDto(s.getSeatNumber(), s.getSeatClass(), s.getIsReserved()))
                    .toList();
        });

        List<SeatDto> seats = seatService.getRecommendedSeats(1L, criteria);

        assertEquals(4, seats.size());
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("1A")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("1B")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("1C")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("11A")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("5B")));
    }

    @Test
    void getRecommendedSeats_ShouldReturnOnlyFreeSeats() {
        SeatFilterCriteria criteria = new SeatFilterCriteria(null, null, null, null, null, null);

        SeatEntity freeSeat1 = new SeatEntity(7L, testFlight, "3A", "Economy", false);
        SeatEntity freeSeat2 = new SeatEntity(8L, testFlight, "4B", "Business", false);
        SeatEntity reservedSeat1 = new SeatEntity(9L, testFlight, "5A", "Economy", true);
        SeatEntity reservedSeat2 = new SeatEntity(10L, testFlight, "6B", "Business", true);

        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(seat1, seat2, seat3, freeSeat1, freeSeat2, reservedSeat1, reservedSeat2));
        when(seatMapper.toDtoList(any())).thenAnswer(invocation -> {
            List<SeatEntity> filteredSeats = invocation.getArgument(0);
            return filteredSeats.stream()
                    .map(s -> new SeatDto(s.getSeatNumber(), s.getSeatClass(), s.getIsReserved()))
                    .toList();
        });

        List<SeatDto> seats = seatService.getRecommendedSeats(1L, criteria);

        assertFalse(seats.isEmpty());
        assertTrue(seats.stream().noneMatch(SeatDto::isReserved));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("5A")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("6B")));
    }

    @Test
    void getRecommendedSeats_ShouldFilterSeatsTogether() {
        SeatFilterCriteria criteria = new SeatFilterCriteria(2, null, null, null, true, null);

        SeatEntity s1 = new SeatEntity(11L, testFlight, "10A", "Economy", false);
        SeatEntity s2 = new SeatEntity(12L, testFlight, "10B", "Economy", false);
        SeatEntity s3 = new SeatEntity(13L, testFlight, "10C", "Economy", false);
        SeatEntity s4 = new SeatEntity(14L, testFlight, "12A", "Economy", false);
        SeatEntity s5 = new SeatEntity(15L, testFlight, "14B", "Economy", false);

        SeatEntity reservedSeat = new SeatEntity(16L, testFlight, "10D", "Economy", true);

        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(s1, s2, s3, s4, s5, reservedSeat));
        when(seatMapper.toDtoList(any())).thenAnswer(invocation -> {
            List<SeatEntity> filteredSeats = invocation.getArgument(0);
            return filteredSeats.stream()
                    .map(s -> new SeatDto(s.getSeatNumber(), s.getSeatClass(), s.getIsReserved()))
                    .toList();
        });

        List<SeatDto> seats = seatService.getRecommendedSeats(1L, criteria);

        assertEquals(2, seats.size());
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("10A")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("10B")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("12A")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("14B")));
    }

    @Test
    void getRecommendedSeats_ShouldReturnThreeSeatsTogether() {
        SeatFilterCriteria criteria = new SeatFilterCriteria(3, null, null, null, true, null);

        SeatEntity s1 = new SeatEntity(17L, testFlight, "15A", "Economy", false);
        SeatEntity s2 = new SeatEntity(18L, testFlight, "15B", "Economy", false);
        SeatEntity s3 = new SeatEntity(19L, testFlight, "15C", "Economy", false);
        SeatEntity s4 = new SeatEntity(20L, testFlight, "18A", "Economy", false);
        SeatEntity s5 = new SeatEntity(21L, testFlight, "18B", "Economy", false);
        SeatEntity reservedSeat = new SeatEntity(22L, testFlight, "18C", "Economy", true);

        when(seatRepository.findByFlightId(1L)).thenReturn(List.of(s1, s2, s3, s4, s5, reservedSeat));
        when(seatMapper.toDtoList(any())).thenAnswer(invocation -> {
            List<SeatEntity> filteredSeats = invocation.getArgument(0);
            return filteredSeats.stream()
                    .map(s -> new SeatDto(s.getSeatNumber(), s.getSeatClass(), s.getIsReserved()))
                    .toList();
        });

        List<SeatDto> seats = seatService.getRecommendedSeats(1L, criteria);

        assertEquals(3, seats.size());
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("15A")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("15B")));
        assertTrue(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("15C")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("18A")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("18B")));
        assertFalse(seats.stream().anyMatch(seat -> seat.getSeatNumber().equals("18C")));
    }
}
