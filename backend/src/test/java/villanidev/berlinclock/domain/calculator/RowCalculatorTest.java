package villanidev.berlinclock.domain.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pure unit tests — no Spring context, each calculator tested in isolation.
 */
@DisplayName("RowCalculators")
class RowCalculatorTest {

    // ─── SecondsLampCalculator ───────────────────────────────────────────────────

    @Nested
    @DisplayName("SecondsLampCalculator")
    class SecondsLamp {

        private final SecondsLampCalculator calc = new SecondsLampCalculator();

        @ParameterizedTest(name = "seconds={0} → {1}")
        @CsvSource({
                "00:00:00,Y",
                "00:00:02,Y",
                "00:00:58,Y",
                "00:00:01,O",
                "00:00:59,O",
                "00:00:23,O"
        })
        void calculate(String time, String expected) {
            assertThat(calc.calculate(LocalTime.parse(time))).isEqualTo(expected);
        }
    }

    // ─── FiveHoursRowCalculator ──────────────────────────────────────────────────

    @Nested
    @DisplayName("FiveHoursRowCalculator")
    class FiveHoursRow {

        private final FiveHoursRowCalculator calc = new FiveHoursRowCalculator();

        @ParameterizedTest(name = "time={0} → {1}")
        @CsvSource({
                "00:00:00,OOOO",
                "23:59:59,RRRR",
                "02:04:00,OOOO",
                "08:23:00,ROOO",
                "16:35:00,RRRO"
        })
        void calculate(String time, String expected) {
            assertThat(calc.calculate(LocalTime.parse(time))).isEqualTo(expected);
        }
    }

    // ─── SingleHoursRowCalculator ────────────────────────────────────────────────

    @Nested
    @DisplayName("SingleHoursRowCalculator")
    class SingleHoursRow {

        private final SingleHoursRowCalculator calc = new SingleHoursRowCalculator();

        @ParameterizedTest(name = "time={0} → {1}")
        @CsvSource({
                "00:00:00,OOOO",
                "23:59:59,RRRO",
                "02:04:00,RROO",
                "08:23:00,RRRO",
                "14:35:00,RRRR"
        })
        void calculate(String time, String expected) {
            assertThat(calc.calculate(LocalTime.parse(time))).isEqualTo(expected);
        }
    }

    // ─── FiveMinutesRowCalculator ────────────────────────────────────────────────

    @Nested
    @DisplayName("FiveMinutesRowCalculator")
    class FiveMinutesRow {

        private final FiveMinutesRowCalculator calc = new FiveMinutesRowCalculator();

        @ParameterizedTest(name = "time={0} → {1}")
        @CsvSource({
                "00:00:00,OOOOOOOOOOO",
                "23:59:59,YYRYYRYYRYY",
                "12:04:00,OOOOOOOOOOO",
                "12:23:00,YYRYOOOOOOO",
                "12:35:00,YYRYYRYOOOO"
        })
        void calculate(String time, String expected) {
            assertThat(calc.calculate(LocalTime.parse(time))).isEqualTo(expected);
        }
    }

    // ─── SingleMinutesRowCalculator ──────────────────────────────────────────────

    @Nested
    @DisplayName("SingleMinutesRowCalculator")
    class SingleMinutesRow {

        private final SingleMinutesRowCalculator calc = new SingleMinutesRowCalculator();

        @ParameterizedTest(name = "time={0} → {1}")
        @CsvSource({
                "00:00:00,OOOO",
                "23:59:59,YYYY",
                "12:32:00,YYOO",
                "12:34:00,YYYY",
                "12:35:00,OOOO"
        })
        void calculate(String time, String expected) {
            assertThat(calc.calculate(LocalTime.parse(time))).isEqualTo(expected);
        }
    }
}
