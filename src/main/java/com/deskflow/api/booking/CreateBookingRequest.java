package com.deskflow.api.booking;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull(message = "deskId is required")
        Long deskId,
        @NotBlank(message = "employeeName must not be blank")
        String employeeName,
        @NotNull(message = "date is required")
        LocalDate date) {
}