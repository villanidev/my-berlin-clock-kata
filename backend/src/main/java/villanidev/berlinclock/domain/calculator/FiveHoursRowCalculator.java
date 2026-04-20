package villanidev.berlinclock.domain.calculator;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import villanidev.berlinclock.domain.RowCalculator;

import java.time.LocalTime;

/**
 * Four-lamp row; each lamp represents 5 hours. All lamps are red.
 */
@Component
public class FiveHoursRowCalculator implements RowCalculator {

    private static final Logger log = LoggerFactory.getLogger(FiveHoursRowCalculator.class);

    @Override
    public String calculate(LocalTime time) {
        String result = RowUtils.buildRow(4, time.getHour() / 5, RowUtils.RED);
        log.trace("FiveHours({}) → {}", time.getHour(), result);
        return result;
    }
}
