package com.deskflow.api.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateBooking() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deskId": 2,
                                  "employeeName": "Chris Lee",
                                  "date": "2026-07-26"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.deskId").value(2))
                .andExpect(jsonPath("$.deskCode").value("HEL-1F-02"))
                .andExpect(jsonPath("$.employeeName").value("Chris Lee"))
                .andExpect(jsonPath("$.date").value("2026-07-26"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturnBadRequestWhenDeskIsInactive() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deskId": 3,
                                  "employeeName": "Chris Lee",
                                  "date": "2026-07-26"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Desk is inactive"));
    }

    @Test
    void shouldReturnBadRequestWhenEmployeeNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deskId": 2,
                                  "employeeName": "   ",
                                  "date": "2026-07-26"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("employeeName must not be blank"));
    }

    @Test
    void shouldReturnNotFoundWhenDeskDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deskId": 999,
                                  "employeeName": "Chris Lee",
                                  "date": "2026-07-26"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Desk not found"));
    }

    @Test
    void shouldReturnConflictWhenDeskIsAlreadyBookedForDate() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deskId": 1,
                                  "employeeName": "Chris Lee",
                                  "date": "2026-07-24"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Desk is already booked for this date"));
    }

                  @Test
                  void shouldCancelBooking() throws Exception {
                    mockMvc.perform(delete("/api/bookings/{id}", 1))
                        .andExpect(status().isNoContent());
                  }

                  @Test
                  void shouldReturnNotFoundWhenCancellingMissingBooking() throws Exception {
                    mockMvc.perform(delete("/api/bookings/{id}", 999))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value("Booking not found"));
                  }
}