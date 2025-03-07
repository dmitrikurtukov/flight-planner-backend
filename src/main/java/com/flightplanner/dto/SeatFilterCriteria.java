package com.flightplanner.dto;


public record SeatFilterCriteria(Integer passengerCount,
                                 Boolean windowPreferred,
                                 Boolean extraLegroom,
                                 Boolean nearExit,
                                 Boolean seatsTogether,
                                 String seatClass) {}
