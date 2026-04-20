package villanidev.berlinclock.domain.calculator;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import villanidev.berlinclock.domain.RowCalculator;

import java.time.LocalTime;

/**
 * Eleven-lamp row; each lamp represents 5 minutes.
 * Every 3rd lit lamp (positions 3, 6, 9) is red to mark quarter-hours;
 * all other lit lamps are yellow.
 */
@Component
public class FiveMinutesRowCalculator implements RowCalculator {

    private static final int ROW_SIZE              = 11;
    private static final int MINUTES_PER_LAMP      = 5;
    private static final int QUARTER_HOUR_INTERVAL = 3;
    private static final Logger log = LoggerFactory.getLogger(FiveMinutesRowCalculator.class);

    @Override
    public String calculate(LocalTime time) {
        int litLamps = time.getMinute() / MINUTES_PER_LAMP;
        char[] row = new char[ROW_SIZE];
        for (int i = 1; i <= ROW_SIZE; i++) {
            if (i <= litLamps) {
                row[i - 1] = (i % QUARTER_HOUR_INTERVAL == 0) ? RowUtils.RED : RowUtils.YELLOW;
            } else {
                row[i - 1] = RowUtils.OFF;
            }
        }
        String result = new String(row);
        log.trace("FiveMinutes({}) -> {}", time.getMinute(), result);
        return result;
    }
}
