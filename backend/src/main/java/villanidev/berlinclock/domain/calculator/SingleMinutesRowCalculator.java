package villanidev.berlinclock.domain.calculator;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import villanidev.berlinclock.domain.RowCalculator;

import java.time.LocalTime;

/**
 * Four-lamp row; each lamp represents 1 minute. All lamps are yellow.
 */
@Component
public class SingleMinutesRowCalculator implements RowCalculator {

    private static final Logger log = LoggerFactory.getLogger(SingleMinutesRowCalculator.class);

    @Override
    public String calculate(LocalTime time) {
        String result = RowUtils.buildRow(4, time.getMinute() % 5, RowUtils.YELLOW);
        log.trace("SingleMinutes({}) → {}", time.getMinute(), result);
        return result;
    }
}
