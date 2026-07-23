package com.deskflow.api.desk;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDeskListWithoutFilters() throws Exception {
        mockMvc.perform(get("/api/desks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].floor").exists())
                .andExpect(jsonPath("$[0].hasMonitor").exists())
                .andExpect(jsonPath("$[0].active").exists());
    }

    @Test
    void shouldFilterDeskListByFloorAndMonitor() throws Exception {
        mockMvc.perform(get("/api/desks")
                        .param("floor", "2")
                        .param("hasMonitor", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].floor").value(2))
                .andExpect(jsonPath("$[0].hasMonitor").value(true))
                .andExpect(jsonPath("$[1].floor").value(2))
                .andExpect(jsonPath("$[1].hasMonitor").value(true));
    }
}