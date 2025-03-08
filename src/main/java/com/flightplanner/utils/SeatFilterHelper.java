package com.flightplanner.utils;

import com.flightplanner.dto.SeatFilterCriteria;
import com.flightplanner.entity.SeatEntity;

import java.util.Comparator;
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

        if (Boolean.TRUE.equals(criteria.seatsTogether()) && criteria.passengerCount() != null && criteria.passengerCount() > 1)
            seats = filterSeatsTogether(seats, criteria.passengerCount());

        return seats;
    }

    private static boolean isWindowSeat(SeatEntity seat) {
        return seat.getSeatNumber().endsWith("A") || seat.getSeatNumber().endsWith("F");
    }

    private static boolean isExtraLegroom(SeatEntity seat) {
        String rowNumber = seat.getSeatNumber().replaceAll("\\D", "");
        return rowNumber.equals("1") || rowNumber.equals("11");
    }

    private static boolean isNearExit(SeatEntity seat) {
        String rowNumber = seat.getSeatNumber().replaceAll("\\D", "");
        return rowNumber.equals("1")
                || rowNumber.equals("2")
                || rowNumber.equals("9")
                || rowNumber.equals("10")
                || rowNumber.equals("11")
                || rowNumber.equals("12")
                || rowNumber.equals("19")
                || rowNumber.equals("20");
    }

    private static List<SeatEntity> filterSeatsTogether(List<SeatEntity> seats, int passengerCount) {
        Map<String, List<SeatEntity>> groupedByRow = seats.stream().collect(Collectors.groupingBy(seat -> seat.getSeatNumber().replaceAll("\\D", "")));
        List<String> sortedRows = groupedByRow.keySet().stream().sorted(Comparator.comparingInt(Integer::parseInt)).toList();

        for (String row : sortedRows) {
            List<SeatEntity> rowSeats = groupedByRow.get(row);
            rowSeats.sort(Comparator.comparing(SeatEntity::getSeatNumber));

            for (int i = 0; i <= rowSeats.size() - passengerCount; i++) {
                List<SeatEntity> possibleGroup = rowSeats.subList(i, i + passengerCount);
                if (isSequential(possibleGroup)) return possibleGroup;
            }
        }

        return seats;
    }

    private static boolean isSequential(List<SeatEntity> seats) {
        for (int i = 0; i < seats.size() - 1; i++) {
            char currentLetter = seats.get(i).getSeatNumber().charAt(seats.get(i).getSeatNumber().length() - 1);
            char nextLetter = seats.get(i + 1).getSeatNumber().charAt(seats.get(i + 1).getSeatNumber().length() - 1);

            if (nextLetter != currentLetter + 1) return false;
        }

        return true;
    }
}
