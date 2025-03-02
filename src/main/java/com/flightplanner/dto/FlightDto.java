package com.flightplanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for flight information transfer")
public class FlightDto {

    @Schema(description = "Flight ID", example = "1")
    private Long id;

    @Schema(description = "Destination of the flight", example = "New York")
    private String destination;

    @Schema(description = "Flight departure time", example = "2025-03-10T15:30:00")
    private LocalDateTime departureTime;

    @Schema(description = "Ticket price", example = "350.50")
    private BigDecimal price;

    @Schema(description = "Flight duration in minutes", example = "120")
    private Integer duration;
}
