package com.flightplanner.utils;

import com.flightplanner.entity.SeatEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatFilterHelper {
    private SeatFilterHelper() {}

    public static List<SeatEntity> filterSeats(List<SeatEntity> seats, Boolean windowPreferred, Boolean extraLegroom, Boolean nearExit, Integer passengerCount, Boolean seatsTogether) {
        if (Boolean.TRUE.equals(windowPreferred))
            seats = filterByWindow(seats);

        if (Boolean.TRUE.equals(extraLegroom))
            seats = filterByExtraLegroom(seats);

        if (Boolean.TRUE.equals(nearExit))
            seats = filterByExit(seats);

        if (Boolean.TRUE.equals(seatsTogether) && passengerCount != null && passengerCount > 1)
            seats = filterSeatsTogether(seats, passengerCount);

        return seats;
    }

    public static List<SeatEntity> filterByWindow(List<SeatEntity> seats) {
        return seats.stream()
                .filter(seat -> seat.getSeatNumber().endsWith("A") || seat.getSeatNumber().endsWith("F"))
                .toList();
    }

    public static List<SeatEntity> filterByExtraLegroom(List<SeatEntity> seats) {
        return seats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("1") || seat.getSeatNumber().startsWith("10"))
                .toList();
    }

    public static List<SeatEntity> filterByExit(List<SeatEntity> seats) {
        return seats.stream()
                .filter(seat -> seat.getSeatNumber().startsWith("1")
                        || seat.getSeatNumber().startsWith("2")
                        || seat.getSeatNumber().startsWith("19")
                        || seat.getSeatNumber().startsWith("20"))
                .toList();
    }

    public static List<SeatEntity> filterSeatsTogether(List<SeatEntity> seats, int passengerCount) {
        if (passengerCount <= 1)
            return seats;


        Map<String, List<SeatEntity>> groupedByRow = seats.stream()
                .collect(Collectors.groupingBy(seat -> seat.getSeatNumber().replaceAll("\\D", "")));

        for (List<SeatEntity> rowSeats : groupedByRow.values()) {
            if (rowSeats.size() >= passengerCount)
                return rowSeats.subList(0, passengerCount);
        }

        return seats;
    }
}
