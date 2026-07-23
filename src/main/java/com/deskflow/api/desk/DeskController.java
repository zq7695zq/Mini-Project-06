package com.deskflow.api.desk;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deskflow.api.booking.BookingRepository;
import com.deskflow.api.common.ApiErrorResponse;

@RestController
@RequestMapping("/api/desks")
public class DeskController {

    private final DeskRepository deskRepository;
    private final BookingRepository bookingRepository;

    public DeskController(DeskRepository deskRepository, BookingRepository bookingRepository) {
        this.deskRepository = deskRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public List<DeskResponse> listDesks(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean hasMonitor) {
        // Route to the narrowest repository query based on the filters provided.
        List<Desk> desks = findDesks(floor, hasMonitor);

        // Map entities to response DTOs so the API shape stays stable.
        return desks.stream()
                .map(DeskResponse::from)
                .toList();
    }

    @GetMapping("/available")
    public ResponseEntity<?> listAvailableDesks(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean hasMonitor) {
        if (date == null || date.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("Missing required parameter: date (format: yyyy-MM-dd)"));
        }

        LocalDate availabilityDate;
        try {
            availabilityDate = LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("Invalid date format. Expected: yyyy-MM-dd, received: " + date));
        }

        Set<Long> bookedDeskIds = bookingRepository.findAllByBookingDateOrderByDesk_CodeAsc(availabilityDate)
                .stream()
                .map(booking -> booking.getDesk().getId())
                .collect(Collectors.toSet());

        List<DeskResponse> availableDesks = findDesks(floor, hasMonitor).stream()
                .filter(Desk::isActive)
                .filter(desk -> !bookedDeskIds.contains(desk.getId()))
                .sorted(Comparator.comparing(Desk::getCode))
                .map(DeskResponse::from)
                .toList();

        return ResponseEntity.ok(new AvailableDesksResponse(
                availabilityDate,
                availableDesks.size(),
                availableDesks));
    }

    private List<Desk> findDesks(Integer floor, Boolean hasMonitor) {
        if (floor != null && hasMonitor != null) {
            return deskRepository.findAllByFloorAndHasMonitor(floor, hasMonitor);
        }

        if (floor != null) {
            return deskRepository.findAllByFloor(floor);
        }

        if (hasMonitor != null) {
            return deskRepository.findAllByHasMonitor(hasMonitor);
        }

        return deskRepository.findAll();
    }
}