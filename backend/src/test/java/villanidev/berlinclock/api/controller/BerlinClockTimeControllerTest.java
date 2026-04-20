package villanidev.berlinclock.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("BerlinClockController")
class BerlinClockTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ─── Feature 1: Digital → Berlin ────────────────────────────────────────────

    @Test
    @DisplayName("to-berlin 00:00:00 → correct flat string and rows")
    void toBerlin_midnight() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-berlin").param("time", "00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digitalTime").value("00:00:00"))
                .andExpect(jsonPath("$.berlinTime").value("YOOOOOOOOOOOOOOOOOOOOOOO"))
                .andExpect(jsonPath("$.seconds").value("Y"))
                .andExpect(jsonPath("$.fiveHours").value("OOOO"))
                .andExpect(jsonPath("$.singleHours").value("OOOO"))
                .andExpect(jsonPath("$.fiveMinutes").value("OOOOOOOOOOO"))
                .andExpect(jsonPath("$.singleMinutes").value("OOOO"));
    }

    @Test
    @DisplayName("to-berlin 23:59:59 → correct flat string and rows")
    void toBerlin_endOfDay() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-berlin").param("time", "23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digitalTime").value("23:59:59"))
                .andExpect(jsonPath("$.berlinTime").value("ORRRRRRROYYRYYRYYRYYYYYY"))
                .andExpect(jsonPath("$.seconds").value("O"))
                .andExpect(jsonPath("$.fiveHours").value("RRRR"))
                .andExpect(jsonPath("$.singleHours").value("RRRO"))
                .andExpect(jsonPath("$.fiveMinutes").value("YYRYYRYYRYY"))
                .andExpect(jsonPath("$.singleMinutes").value("YYYY"));
    }

    @Test
    @DisplayName("to-berlin invalid format → 400")
    void toBerlin_invalidFormat_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-berlin").param("time", "25:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("to-berlin bad string → 400")
    void toBerlin_badString_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-berlin").param("time", "noon"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // ─── Feature 2: Berlin → Digital ────────────────────────────────────────────

    @Test
    @DisplayName("to-digital YOOO…OOO → 00:00:00")
    void toDigital_midnight() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-digital").param("clock", "YOOOOOOOOOOOOOOOOOOOOOOO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value("00:00:00"));
    }

    @Test
    @DisplayName("to-digital ORRR…YYY → 23:59:01")
    void toDigital_endOfDay() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-digital").param("clock", "ORRRRRRROYYRYYRYYRYYYYYY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value("23:59:01"));
    }

    @Test
    @DisplayName("to-digital invalid chars → 400")
    void toDigital_invalidChars_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-digital").param("clock", "XOOOOOOOOOOOOOOOOOOOOOOO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("to-digital wrong length → 400")
    void toDigital_wrongLength_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/berlin-clock/to-digital").param("clock", "YOOOO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
