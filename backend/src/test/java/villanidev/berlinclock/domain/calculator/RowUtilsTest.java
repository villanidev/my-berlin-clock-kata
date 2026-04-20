package villanidev.berlinclock.domain.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RowUtils")
class RowUtilsTest {

    // ─── Constants ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("constants have the correct char values")
    void constants() {
        assertThat(RowUtils.YELLOW).isEqualTo('Y');
        assertThat(RowUtils.RED).isEqualTo('R');
        assertThat(RowUtils.OFF).isEqualTo('O');
    }

    @Test
    @DisplayName("cannot be instantiated")
    void notInstantiable() throws Exception {
        Constructor<RowUtils> ctor = RowUtils.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        assertThatThrownBy(ctor::newInstance)
                .cause()
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // ─── countLit ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("countLit")
    class CountLit {

        @Test
        @DisplayName("empty string returns 0")
        void empty() {
            assertThat(RowUtils.countLit("")).isZero();
        }

        @Test
        @DisplayName("all OFF returns 0")
        void allOff() {
            assertThat(RowUtils.countLit("OOOO")).isZero();
        }

        @Test
        @DisplayName("all YELLOW returns full count")
        void allYellow() {
            assertThat(RowUtils.countLit("YYYY")).isEqualTo(4);
        }

        @Test
        @DisplayName("all RED returns full count")
        void allRed() {
            assertThat(RowUtils.countLit("RRR")).isEqualTo(3);
        }

        @ParameterizedTest(name = "\"{0}\" -> {1} lit")
        @CsvSource({
                "YYROOO,  3",
                "YYRYYRYYROO, 9",
                "YOOOO, 1",
                "OOOOY, 1",
        })
        @DisplayName("mixed row returns correct lit count")
        void mixed(String row, int expected) {
            assertThat(RowUtils.countLit(row)).isEqualTo(expected);
        }
    }

    // ─── buildRow ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("buildRow")
    class BuildRow {

        @Test
        @DisplayName("lit=0 returns all OFF")
        void noLit() {
            assertThat(RowUtils.buildRow(4, 0, RowUtils.YELLOW)).isEqualTo("OOOO");
        }

        @Test
        @DisplayName("lit=total returns all onChar")
        void allLit() {
            assertThat(RowUtils.buildRow(4, 4, RowUtils.RED)).isEqualTo("RRRR");
        }

        @ParameterizedTest(name = "total={0}, lit={1}, char={2} -> \"{3}\"")
        @CsvSource({
                "4, 2, Y, YYOO",
                "4, 3, R, RRRO",
                "1, 1, Y, Y",
                "1, 0, R, O",
                "5, 5, Y, YYYYY",
        })
        @DisplayName("partial fill produces correct row string")
        void partialFill(int total, int lit, char onChar, String expected) {
            assertThat(RowUtils.buildRow(total, lit, onChar)).isEqualTo(expected);
        }
    }
}
