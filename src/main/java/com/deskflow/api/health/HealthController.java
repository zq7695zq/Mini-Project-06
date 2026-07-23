package com.deskflow.api.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                Map<String, String> response = new LinkedHashMap<>();
                response.put("status", "ok");
                response.put("database", "ok");
                return ResponseEntity.ok(response);
            }
        } catch (DataAccessException exception) {
            // Fall through to the down response when the database is unreachable.
        }

        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "down");
        response.put("database", "down");
        return ResponseEntity.status(503).body(response);
    }
}