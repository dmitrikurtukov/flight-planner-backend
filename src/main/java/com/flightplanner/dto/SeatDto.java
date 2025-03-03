package com.flightplanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for seat information transfer")
public class SeatDto {

    @Schema(description = "Seat number", example = "1A")
    private String seatNumber;

    @Schema(description = "Seat class", example = "Economy")
    private String seatClass;

    @Schema(description = "Seat reservation status", example = "false")
    private boolean isReserved;
}
