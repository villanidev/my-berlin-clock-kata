package villanidev.berlinclock.application;

import villanidev.berlinclock.application.usecases.BerlinClockServiceImpl;
import villanidev.berlinclock.domain.BerlinClockTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import villanidev.berlinclock.domain.calculator.*;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the orchestrator service. Verifies that toBerlinClock assembles
 * the correct flat string, and that toDigitalTime parses clock strings correctly.
 */
@DisplayName("BerlinClockService")
class BerlinClockServiceTest {

    private BerlinClockServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BerlinClockServiceImpl(
                new SecondsLampCalculator(),
                new FiveHoursRowCalculator(),
                new SingleHoursRowCalculator(),
                new FiveMinutesRowCalculator(),
                new SingleMinutesRowCalculator()
        );
    }

    // ─── Digital → Berlin (Feature 1) ───────────────────────────────────────────

    @Nested
    @DisplayName("toBerlinClock")
    class ToBerlinClockTime {

        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({
                "00:00:00,YOOOOOOOOOOOOOOOOOOOOOOO",
                "23:59:59,ORRRRRRROYYRYYRYYRYYYYYY",
                "16:50:06,YRRROROOOYYRYYRYYRYOOOOO",
                "11:37:01,ORROOROOOYYRYYRYOOOOYYOO"
        })
        void flatStringMatchesSpec(String time, String expected) {
            BerlinClockTime result = service.toBerlinClock(LocalTime.parse(time));
            assertThat(result.toFlatString()).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{0} → rows")
        @CsvSource({
                "00:00:00, Y, OOOO, OOOO, OOOOOOOOOOO, OOOO",
                "23:59:59, O, RRRR, RRRO, YYRYYRYYRYY, YYYY",
                "16:50:06, Y, RRRO, ROOO, YYRYYRYYRYO, OOOO",
                "11:37:01, O, RROO, ROOO, YYRYYRYO OOO, YYOO"
        })
        void individualRowsAreExposed(String time, String sec, String fiveH, String oneH,
                                       String fiveM, String oneM) {
            BerlinClockTime result = service.toBerlinClock(LocalTime.parse(time));
            assertThat(result.seconds()).isEqualTo(sec.trim());
            assertThat(result.fiveHours()).isEqualTo(fiveH.trim());
            assertThat(result.singleHours()).isEqualTo(oneH.trim());
            assertThat(result.fiveMinutes()).isEqualTo(fiveM.replace(" ", ""));
            assertThat(result.singleMinutes()).isEqualTo(oneM.trim());
        }
    }

    // ─── Berlin → Digital (Feature 2) ───────────────────────────────────────────

    @Nested
    @DisplayName("toDigitalTime")
    class ToDigitalTime {

        // NOTE: Seconds parity only — Y→00 (even), O→01 (odd). Exact seconds lost.
        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({
                "YOOOOOOOOOOOOOOOOOOOOOOO,00:00:00",
                "ORRRRRRROYYRYYRYYRYYYYYY,23:59:01",  // 59 is odd → 01
                "YRRROROOOYYRYYRYYRYOOOOO,16:50:00",  // 6 is even → 00
                "ORROOROOOYYRYYRYOOOOYYOO,11:37:01"   // 1 is odd  → 01
        })
        void toDigitalTime(String clock, String expected) {
            assertThat(service.toDigitalTime(clock)).isEqualTo(expected);
        }
    }
}
