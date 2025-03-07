package com.flightplanner.utils;

import com.flightplanner.dto.FlightFilterCriteria;
import com.flightplanner.entity.FlightEntity;

import java.util.Comparator;
import java.util.List;

public class FlightFilterHelper {
    private FlightFilterHelper() {}

    public static List<FlightEntity> filterFlights(List<FlightEntity> flights, FlightFilterCriteria criteria) {
        return flights.stream()
                .filter(flight -> criteria.destination() == null || flight.getDestination().equalsIgnoreCase(criteria.destination()))
                .filter(flight -> criteria.departureDate() == null || flight.getDepartureTime().toLocalDate().toString().equals(criteria.departureDate()))
                .filter(flight -> criteria.minDuration() == null || flight.getDuration() >= criteria.minDuration())
                .filter(flight -> criteria.maxDuration() == null || flight.getDuration() <= criteria.maxDuration())
                .filter(flight -> criteria.minPrice() == null || flight.getPrice().doubleValue() >= criteria.minPrice())
                .filter(flight -> criteria.maxPrice() == null || flight.getPrice().doubleValue() <= criteria.maxPrice())
                .toList();
    }

    public static List<FlightEntity> sortFlights(List<FlightEntity> flights, String sortBy) {
        if (sortBy == null)
            return flights;

        return switch (sortBy) {
            case "price_asc" -> flights.stream()
                    .sorted(Comparator.comparing(FlightEntity::getPrice))
                    .toList();
            case "price_desc" -> flights.stream()
                    .sorted(Comparator.comparing(FlightEntity::getPrice).reversed())
                    .toList();
            case "time_asc" -> flights.stream()
                    .sorted(Comparator.comparing(FlightEntity::getDepartureTime))
                    .toList();
            case "time_desc" -> flights.stream()
                    .sorted(Comparator.comparing(FlightEntity::getDepartureTime).reversed())
                    .toList();
            default -> throw new IllegalArgumentException(String.format("Invalid sortBy value: %s", sortBy));
        };
    }
}
