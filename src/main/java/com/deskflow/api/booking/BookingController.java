package com.deskflow.api.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deskflow.api.common.ApiErrorResponse;
import com.deskflow.api.desk.Desk;
import com.deskflow.api.desk.DeskRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final DeskRepository deskRepository;

    public BookingController(BookingRepository bookingRepository, DeskRepository deskRepository) {
        this.bookingRepository = bookingRepository;
        this.deskRepository = deskRepository;
    }

    @GetMapping
    public ResponseEntity<?> getBookingsByDate(@RequestParam(required = false) String date) {
        if (date == null || date.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("Missing required parameter: date (format: yyyy-MM-dd)"));
        }

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("Invalid date format. Expected: yyyy-MM-dd, received: " + date));
        }

        List<Map<String, Object>> bookings = bookingRepository.findAllByBookingDateOrderByDesk_CodeAsc(bookingDate)
                .stream()
                .map(booking -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", booking.getId());
                    row.put("deskId", booking.getDesk().getId());
                    row.put("deskCode", booking.getDesk().getCode());
                    row.put("floor", booking.getDesk().getFloor());
                    row.put("employeeName", booking.getEmployeeName());
                    row.put("bookingDate", booking.getBookingDate().toString());
                    row.put("createdAt", booking.getCreatedAt().toString());
                    return row;
                })
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("date", bookingDate.toString());
        response.put("message", bookings.isEmpty()
                ? "当前日期 " + bookingDate + " 暂无任何预定记录"
                : "共找到 " + bookings.size() + " 条预定记录");
        response.put("bookings", bookings);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        Desk desk = deskRepository.findById(request.deskId())
                .orElse(null);

        if (desk == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse("Desk not found"));
        }

        if (!desk.isActive()) {
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("Desk is inactive"));
        }

        if (bookingRepository.existsByDesk_IdAndBookingDate(request.deskId(), request.date())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse("Desk is already booked for this date"));
        }

        Booking booking = new Booking();
        booking.setDesk(desk);
        booking.setEmployeeName(request.employeeName().trim());
        booking.setBookingDate(request.date());
        booking.setCreatedAt(LocalDateTime.now());

        try {
            Booking savedBooking = bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(BookingResponse.from(savedBooking));
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse("Desk is already booked for this date"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElse(null);

        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse("Booking not found"));
        }

        bookingRepository.delete(booking);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed";
        return ResponseEntity.badRequest().body(new ApiErrorResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse("Malformed request body"));
    }
}
