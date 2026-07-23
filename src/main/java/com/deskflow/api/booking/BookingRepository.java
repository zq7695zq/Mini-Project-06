package com.deskflow.api.booking;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByDesk_IdAndBookingDate(Long deskId, LocalDate bookingDate);

    List<Booking> findAllByBookingDateOrderByDesk_CodeAsc(LocalDate bookingDate);

    List<Booking> findAllByEmployeeNameIgnoreCaseOrderByBookingDateAsc(String employeeName);
}