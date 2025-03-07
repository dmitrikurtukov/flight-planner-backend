package com.flightplanner.utils;

import com.flightplanner.dto.SeatFilterCriteria;
import com.flightplanner.entity.SeatEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatFilterHelper {
    private SeatFilterHelper() {}

    public static List<SeatEntity> filterSeats(List<SeatEntity> seats, SeatFilterCriteria criteria) {
        seats = seats.stream()
                .filter(seat -> criteria.windowPreferred() == null || isWindowSeat(seat))
                .filter(seat -> criteria.extraLegroom() == null || isExtraLegroom(seat))
                .filter(seat -> criteria.nearExit() == null || isNearExit(seat))
                .filter(seat -> criteria.seatClass() == null || seat.getSeatClass().equalsIgnoreCase(criteria.seatClass()))
                .toList();

        if (Boolean.TRUE.equals(criteria.seatsTogether()) && criteria.passengerCount() != null && criteria.passengerCount() > 1) {
            seats = filterSeatsTogether(seats, criteria.passengerCount());
        }

        return seats;
    }

    private static boolean isWindowSeat(SeatEntity seat) {
        return seat.getSeatNumber().endsWith("A") || seat.getSeatNumber().endsWith("F");
    }

    private static boolean isExtraLegroom(SeatEntity seat) {
        return seat.getSeatNumber().startsWith("1") || seat.getSeatNumber().startsWith("10");
    }

    private static boolean isNearExit(SeatEntity seat) {
        return seat.getSeatNumber().startsWith("1")
                || seat.getSeatNumber().startsWith("2")
                || seat.getSeatNumber().startsWith("19")
                || seat.getSeatNumber().startsWith("20");
    }

    public static List<SeatEntity> filterSeatsTogether(List<SeatEntity> seats, int passengerCount) {
        Map<String, List<SeatEntity>> groupedByRow = seats.stream()
                .collect(Collectors.groupingBy(seat -> seat.getSeatNumber().replaceAll("\\D", "")));

        for (List<SeatEntity> rowSeats : groupedByRow.values()) {
            if (rowSeats.size() >= passengerCount)
                return rowSeats.subList(0, passengerCount);
        }

        return seats;
    }
}
