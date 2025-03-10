package com.flightplanner.config;

import com.flightplanner.entity.FlightEntity;
import com.flightplanner.entity.SeatEntity;
import com.flightplanner.repository.FlightRepository;
import com.flightplanner.repository.SeatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final Random random = new Random();

    private static final List<String> DESTINATIONS = List.of("New York", "London", "Tokyo", "Paris", "Berlin", "Dubai", "Sydney", "Toronto", "Miami", "Madrid", "Tallinn", "Helsinki");

    @PostConstruct
    public void loadData() {
        if (flightRepository.count() == 0) {
            generateFlights();
        }
    }

    private void generateFlights() {
        for (int i = 0; i < 10; i++) {
            FlightEntity flight = new FlightEntity();
            flight.setDestination(randomDestination());
            flight.setDepartureTime(randomDepartureTime());
            flight.setPrice(randomPrice());
            flight.setDuration(randomDuration());

            flightRepository.save(flight);

            generateSeatsForFlight(flight);
        }
    }

    private void generateSeatsForFlight(FlightEntity flight) {
        List<SeatEntity> seats = new ArrayList<>();
        int rows = 20;
        char[] seatColumns = {'A', 'B', 'C', 'D', 'E', 'F'};

        for (int row = 1; row <= rows; row++) {
            for (char col : seatColumns) {
                SeatEntity seat = new SeatEntity();
                seat.setFlight(flight);
                seat.setSeatNumber(row + String.valueOf(col));

                if (row <= 2)
                    seat.setSeatClass("First Class");
                else if (row <= 5)
                    seat.setSeatClass("Business");
                else
                    seat.setSeatClass("Economy");

                seat.setIsReserved(random.nextDouble() < 0.3); // 30% of flight seats will be reserved

                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }

    private String randomDestination() {
        return DESTINATIONS.get(random.nextInt(DESTINATIONS.size()));
    }

    private LocalDateTime randomDepartureTime() {
        return LocalDateTime.now().plusDays(random.nextInt(30) + (long) 1)
                .withHour(random.nextInt(24))
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    private BigDecimal randomPrice() {
        int price = 100 + random.nextInt(401); // 100 - 500
        return BigDecimal.valueOf(roundToNearestFive(price));
    }

    private int randomDuration() {
        int duration = 60 + random.nextInt(181); // 60 - 240
        return roundToNearestFive(duration);
    }

    private int roundToNearestFive(int number) {
        return (int) (Math.round(number / 5.0) * 5);
    }
}
