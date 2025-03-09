package com.flightplanner.service;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.dto.FlightFilterCriteria;
import com.flightplanner.entity.FlightEntity;
import com.flightplanner.mappers.FlightMapper;
import com.flightplanner.repository.FlightRepository;
import com.flightplanner.utils.FlightFilterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightService flightService;

    private FlightEntity testFlight;

    @BeforeEach
    void setUp() {
        testFlight = new FlightEntity();
        testFlight.setId(1L);
        testFlight.setDestination("Tokyo");
        testFlight.setDepartureTime(LocalDateTime.of(2025, 3, 20, 12, 0));
        testFlight.setDuration(120);
        testFlight.setPrice(BigDecimal.valueOf(200));
    }

    @Test
    void getFlights_ShouldReturnFlights() {
        List<FlightEntity> flightEntities = List.of(testFlight);
        FlightFilterCriteria criteria = new FlightFilterCriteria("Tokyo", null, null, null, null, null, null);
        FlightDto expectedDto = new FlightDto(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(200), 120);

        when(flightRepository.findAll()).thenReturn(flightEntities);
        when(flightMapper.toDtoList(any())).thenReturn(List.of(expectedDto));

        List<FlightDto> flights = flightService.getFlights(criteria);

        assertFalse(flights.isEmpty());
        assertEquals(1, flights.size());
        assertEquals("Tokyo", flights.getFirst().getDestination());

        verify(flightRepository, times(1)).findAll();
        verify(flightMapper, times(1)).toDtoList(any());
    }

    @Test
    void getFlights_ShouldFilterByPriceAndDuration() {
        List<FlightEntity> flightEntities = List.of(
                new FlightEntity(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(100), 120, null),
                new FlightEntity(2L, "Tokyo", LocalDateTime.of(2025, 3, 21, 14, 0), BigDecimal.valueOf(300), 180, null)
        );
        FlightFilterCriteria criteria = new FlightFilterCriteria("Tokyo", null, 100, 150, 100.0, 200.0, null);

        when(flightRepository.findAll()).thenReturn(flightEntities);
        when(flightMapper.toDtoList(any())).thenReturn(List.of(new FlightDto(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(100), 120)));

        List<FlightDto> flights = flightService.getFlights(criteria);

        assertEquals(1, flights.size());
        assertEquals(100, flights.getFirst().getPrice().doubleValue());
        assertEquals(120, flights.getFirst().getDuration());
    }

    @Test
    void getFlights_ShouldSortByPriceAscending() {
        List<FlightEntity> flightEntities = List.of(
                new FlightEntity(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(300), 120, null),
                new FlightEntity(2L, "Tokyo", LocalDateTime.of(2025, 3, 21, 14, 0), BigDecimal.valueOf(200), 180, null)
        );
        FlightFilterCriteria criteria = new FlightFilterCriteria(null, null, null, null, null, null, "price_asc");

        when(flightRepository.findAll()).thenReturn(flightEntities);
        when(flightMapper.toDtoList(any())).thenAnswer(_ -> {
            List<FlightEntity> sortedFlights = FlightFilterHelper.sortFlights(flightEntities, "price_asc");
            return sortedFlights.stream().map(f -> new FlightDto(f.getId(), f.getDestination(), f.getDepartureTime(), f.getPrice(), f.getDuration())).toList();
        });

        List<FlightDto> flights = flightService.getFlights(criteria);

        assertEquals(2, flights.size());
        assertEquals(200, flights.get(0).getPrice().doubleValue());
        assertEquals(300, flights.get(1).getPrice().doubleValue());
    }

    @Test
    void getFlights_ShouldFilterByDepartureDate() {
        List<FlightEntity> flightEntities = List.of(
                new FlightEntity(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(100), 120, null),
                new FlightEntity(2L, "Tokyo", LocalDateTime.of(2025, 3, 21, 14, 0), BigDecimal.valueOf(300), 180, null)
        );
        FlightFilterCriteria criteria = new FlightFilterCriteria(null, "2025-03-20", null, null, null, null, null);

        when(flightRepository.findAll()).thenReturn(flightEntities);
        when(flightMapper.toDtoList(any())).thenReturn(List.of(new FlightDto(1L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(100), 120)));

        List<FlightDto> flights = flightService.getFlights(criteria);

        assertEquals(1, flights.size());
        assertEquals("2025-03-20T12:00", flights.getFirst().getDepartureTime().toString());
    }

    @Test
    void getFlights_ShouldSortByDepartureTimeAscending() {
        List<FlightEntity> flightEntities = List.of(
                new FlightEntity(1L, "Tokyo", LocalDateTime.of(2025, 3, 21, 14, 0), BigDecimal.valueOf(200), 120, null),
                new FlightEntity(2L, "Tokyo", LocalDateTime.of(2025, 3, 20, 12, 0), BigDecimal.valueOf(300), 180, null)
        );
        FlightFilterCriteria criteria = new FlightFilterCriteria(null, null, null, null, null, null, "time_asc");

        when(flightRepository.findAll()).thenReturn(flightEntities);
        when(flightMapper.toDtoList(any())).thenAnswer(_ -> {
            List<FlightEntity> sortedFlights = FlightFilterHelper.sortFlights(flightEntities, "time_asc");
            return sortedFlights.stream().map(f -> new FlightDto(f.getId(), f.getDestination(), f.getDepartureTime(), f.getPrice(), f.getDuration())).toList();
        });

        List<FlightDto> flights = flightService.getFlights(criteria);

        assertEquals(2, flights.size());
        assertEquals(LocalDateTime.of(2025, 3, 20, 12, 0), flights.get(0).getDepartureTime());
        assertEquals(LocalDateTime.of(2025, 3, 21, 14, 0), flights.get(1).getDepartureTime());
    }

    @Test
    void getFlights_ShouldThrowExceptionForInvalidSortBy() {
        FlightFilterCriteria criteria = new FlightFilterCriteria(null, null, null, null, null, null, "invalid_value");

        when(flightRepository.findAll()).thenReturn(List.of(testFlight));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> flightService.getFlights(criteria));

        assertTrue(exception.getMessage().contains("Invalid sortBy value"));
    }
}
