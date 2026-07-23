package com.deskflow.api.booking;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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