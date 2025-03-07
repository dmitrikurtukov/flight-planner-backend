package com.flightplanner.controller;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.dto.SeatFilterCriteria;
import com.flightplanner.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Tag(name = "Seats", description = "Seat management APIs")
public class SeatController {
    private final SeatService seatService;

    @Operation(
            summary = "Get seats by flight ID with optional recommendations",
            description = "Retrieve a list of available seats for a given flight. If filters are provided, returns recommended seats based on criteria."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved seats")
    @ApiResponse(responseCode = "400", description = "Invalid flight ID format")
    @GetMapping
    public ResponseEntity<List<SeatDto>> getSeatsByFlightId(
            @RequestParam Long flightId,
            @RequestParam(required = false) Integer passengerCount,
            @RequestParam(required = false) Boolean windowPreferred,
            @RequestParam(required = false) Boolean extraLegroom,
            @RequestParam(required = false) Boolean nearExit,
            @RequestParam(required = false) Boolean seatsTogether,
            @RequestParam(required = false) String seatClass
    ) {
        SeatFilterCriteria criteria = new SeatFilterCriteria(passengerCount, windowPreferred, extraLegroom, nearExit, seatsTogether, seatClass);
        return ResponseEntity.ok(seatService.getRecommendedSeatsForFlight(flightId, criteria));
    }
}
