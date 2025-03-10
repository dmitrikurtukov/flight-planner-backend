package com.flightplanner.controller;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.dto.FlightFilterCriteria;
import com.flightplanner.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Flight management APIs")
public class FlightController {
    private final FlightService flightService;

    @Operation(
            summary = "Get all flights or filter by destination, date, duration, and price",
            description = "Retrieve all available flights. If filters are provided, return only matching flights."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of flights")
    @GetMapping
    public ResponseEntity<List<FlightDto>> getFlights(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        FlightFilterCriteria criteria = new FlightFilterCriteria(destination, departureDate, minDuration, maxDuration, minPrice, maxPrice, sortBy);
        return ResponseEntity.ok(flightService.getFlights(criteria));
    }
}
