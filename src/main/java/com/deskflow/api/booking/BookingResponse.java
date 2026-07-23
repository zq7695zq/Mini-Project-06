package com.deskflow.api.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long deskId,
        String deskCode,
        String employeeName,
        LocalDate date,
        LocalDateTime createdAt) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getDesk().getId(),
                booking.getDesk().getCode(),
                booking.getEmployeeName(),
                booking.getBookingDate(),
                booking.getCreatedAt());
    }
}