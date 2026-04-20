package villanidev.berlinclock.domain.calculator;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import villanidev.berlinclock.domain.RowCalculator;

import java.time.LocalTime;

/**
 * Top circular lamp: {@code Y} on even seconds, {@code O} on odd seconds.
 */
@Component
public class SecondsLampCalculator implements RowCalculator {

    private static final Logger log = LoggerFactory.getLogger(SecondsLampCalculator.class);

    @Override
    public String calculate(LocalTime time) {
        String result = time.getSecond() % 2 == 0
                ? String.valueOf(RowUtils.YELLOW)
                : String.valueOf(RowUtils.OFF);
        log.trace("SecondsLamp({}) → {}", time.getSecond(), result);
        return result;
    }
}
