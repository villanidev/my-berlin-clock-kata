package villanidev.berlinclock.application.usecases;

import villanidev.berlinclock.application.BerlinClockService;
import villanidev.berlinclock.domain.BerlinClockTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import villanidev.berlinclock.domain.RowCalculator;
import villanidev.berlinclock.domain.calculator.*;
import static villanidev.berlinclock.domain.calculator.RowUtils.YELLOW;
import static villanidev.berlinclock.domain.calculator.RowUtils.countLit;

import java.time.LocalTime;

/**
 * Orchestrates the five {@link RowCalculator} implementations to assemble a
 * {@link BerlinClockTime} domain record. Contains no row-level logic itself —
 * adding a new display rule means adding a new calculator, not touching this class.
 */
@Service
public class BerlinClockServiceImpl implements BerlinClockService {

    private static final Logger log = LoggerFactory.getLogger(BerlinClockServiceImpl.class);

    // Berlin Clock string layout: [0] seconds | [1-4] fiveHours | [5-8] singleHours | [9-19] fiveMinutes | [20-23] singleMinutes
    private static final int IDX_LAMP_START = 0;
    private static final int IDX_FIVE_HOURS_START     = 1;
    private static final int IDX_FIVE_HOURS_END       = 5;
    private static final int IDX_SINGLE_HOURS_START   = 5;
    private static final int IDX_SINGLE_HOURS_END     = 9;
    private static final int IDX_FIVE_MINUTES_START   = 9;
    private static final int IDX_FIVE_MINUTES_END     = 20;
    private static final int IDX_SINGLE_MINUTES_START = 20;
    private static final int IDX_SINGLE_MINUTES_END   = 24;

    private static final int    LAMPS_PER_GROUP     = 5;
    private static final String DIGITAL_TIME_FORMAT = "%02d:%02d:%02d";

    private final RowCalculator secondsLamp;
    private final RowCalculator fiveHours;
    private final RowCalculator singleHours;
    private final RowCalculator fiveMinutes;
    private final RowCalculator singleMinutes;

    public BerlinClockServiceImpl(
            SecondsLampCalculator secondsLamp,
            FiveHoursRowCalculator fiveHours,
            SingleHoursRowCalculator singleHours,
            FiveMinutesRowCalculator fiveMinutes,
            SingleMinutesRowCalculator singleMinutes) {
        this.secondsLamp   = secondsLamp;
        this.fiveHours     = fiveHours;
        this.singleHours   = singleHours;
        this.fiveMinutes   = fiveMinutes;
        this.singleMinutes = singleMinutes;
    }

    @Override
    public BerlinClockTime toBerlinClock(LocalTime time) {
        log.trace("toBerlinClock({})", time);
        return new BerlinClockTime(
                secondsLamp.calculate(time),
                fiveHours.calculate(time),
                singleHours.calculate(time),
                fiveMinutes.calculate(time),
                singleMinutes.calculate(time)
        );
    }

    @Override
    public String toDigitalTime(String clockString) {
        log.trace("toDigitalTime({})", clockString);
        char   secondsLamp      = clockString.charAt(IDX_LAMP_START);
        String fiveHoursRow     = clockString.substring(IDX_FIVE_HOURS_START,     IDX_FIVE_HOURS_END);
        String singleHoursRow   = clockString.substring(IDX_SINGLE_HOURS_START,   IDX_SINGLE_HOURS_END);
        String fiveMinutesRow   = clockString.substring(IDX_FIVE_MINUTES_START,   IDX_FIVE_MINUTES_END);
        String singleMinutesRow = clockString.substring(IDX_SINGLE_MINUTES_START, IDX_SINGLE_MINUTES_END);

        int seconds = (secondsLamp == YELLOW) ? 0 : 1;
        int hours   = countLit(fiveHoursRow)   * LAMPS_PER_GROUP + countLit(singleHoursRow);
        int minutes = countLit(fiveMinutesRow) * LAMPS_PER_GROUP + countLit(singleMinutesRow);

        return String.format(DIGITAL_TIME_FORMAT, hours, minutes, seconds);
    }

}
