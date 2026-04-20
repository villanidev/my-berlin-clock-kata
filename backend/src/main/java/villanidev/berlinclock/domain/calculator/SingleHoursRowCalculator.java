package villanidev.berlinclock.domain.calculator;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import villanidev.berlinclock.domain.RowCalculator;

import java.time.LocalTime;

/**
 * Four-lamp row; each lamp represents 1 hour. All lamps are red.
 */
@Component
public class SingleHoursRowCalculator implements RowCalculator {

    private static final Logger log = LoggerFactory.getLogger(SingleHoursRowCalculator.class);

    @Override
    public String calculate(LocalTime time) {
        String result = RowUtils.buildRow(4, time.getHour() % 5, RowUtils.RED);
        log.trace("SingleHours({}) → {}", time.getHour(), result);
        return result;
    }
}
