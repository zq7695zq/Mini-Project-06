package com.deskflow.api.booking;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final JdbcTemplate jdbcTemplate;

    public BookingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBookingsByDate(
            @RequestParam(required = false) String date) {

        if (date == null || date.isBlank()) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Missing required parameter: date (format: yyyy-MM-dd)");
            return ResponseEntity.badRequest().body(error);
        }

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Invalid date format. Expected: yyyy-MM-dd, received: " + date);
            return ResponseEntity.badRequest().body(error);
        }

        String sql = """
                SELECT b.id,
                       b.desk_id   AS deskId,
                       d.code      AS deskCode,
                       d.floor     AS floor,
                       b.employee_name AS employeeName,
                       b.booking_date  AS bookingDate,
                       b.created_at    AS createdAt
                FROM booking b
                JOIN desk d ON b.desk_id = d.id
                WHERE b.booking_date = ?
                ORDER BY d.code
                """;

        List<Map<String, Object>> bookings = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", rs.getLong("id"));
            row.put("deskId", rs.getLong("deskId"));
            row.put("deskCode", rs.getString("deskCode"));
            row.put("floor", rs.getInt("floor"));
            row.put("employeeName", rs.getString("employeeName"));
            row.put("bookingDate", rs.getString("bookingDate"));
            row.put("createdAt", rs.getString("createdAt"));
            return row;
        }, bookingDate.toString());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("date", bookingDate.toString());

        if (bookings.isEmpty()) {
            response.put("message", "当前日期 " + bookingDate + " 暂无任何预定记录");
        } else {
            response.put("message", "共找到 " + bookings.size() + " 条预定记录");
        }

        response.put("bookings", bookings);
        return ResponseEntity.ok(response);
    }
}
