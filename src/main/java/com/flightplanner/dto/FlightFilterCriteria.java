package com.flightplanner.dto;

public record FlightFilterCriteria(String destination,
                                   String departureDate,
                                   Integer minDuration,
                                   Integer maxDuration,
                                   Double minPrice,
                                   Double maxPrice,
                                   String sortBy) {}
