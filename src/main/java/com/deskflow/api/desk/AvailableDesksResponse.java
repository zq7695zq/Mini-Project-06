package com.deskflow.api.desk;

import java.time.LocalDate;
import java.util.List;

public record AvailableDesksResponse(
        LocalDate date,
        int count,
        List<DeskResponse> desks) {
}