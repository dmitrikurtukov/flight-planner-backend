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
            summary = "Get recommended seats by flight ID",
            description = "Retrieve a list of recommended seats for a given flight based on criteria."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recommended seats")
    @ApiResponse(responseCode = "400", description = "Invalid flight ID format")
    @GetMapping
    public ResponseEntity<List<SeatDto>> getRecommendedSeatsByFlightId(
            @RequestParam Long flightId,
            @RequestParam(required = false) Integer passengerCount,
            @RequestParam(required = false) Boolean windowPreferred,
            @RequestParam(required = false) Boolean extraLegroom,
            @RequestParam(required = false) Boolean nearExit,
            @RequestParam(required = false) Boolean seatsTogether,
            @RequestParam(required = false) String seatClass
    ) {
        SeatFilterCriteria criteria = new SeatFilterCriteria(passengerCount, windowPreferred, extraLegroom, nearExit, seatsTogether, seatClass);
        return ResponseEntity.ok(seatService.getRecommendedSeats(flightId, criteria));
    }

    @Operation(
            summary = "Get all seats by flight ID",
            description = "Retrieve a list of all seats for a given flight."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all seats")
    @ApiResponse(responseCode = "400", description = "Invalid flight ID format")
    @GetMapping("/all")
    public ResponseEntity<List<SeatDto>> getAllSeats(@RequestParam Long flightId) {
        return ResponseEntity.ok(seatService.getAllSeats(flightId));
    }
}
