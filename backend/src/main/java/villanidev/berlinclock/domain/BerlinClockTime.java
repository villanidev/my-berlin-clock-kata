package villanidev.berlinclock.domain;

/**
 * Immutable domain model representing a point in time expressed as a Berlin Clock.
 *
 * <p>Layout of {@link #toFlatString()} (24 chars):
 * <pre>
 *   [0]     – seconds lamp   (Y/O)
 *   [1-4]   – five-hours row (RRRR)
 *   [5-8]   – one-hour row   (RRRR)
 *   [9-19]  – five-minutes row (YYRYYRYYRYY)
 *   [20-23] – one-minute row (YYYY)
 * </pre>
 */
public record BerlinClockTime(
        String seconds,
        String fiveHours,
        String singleHours,
        String fiveMinutes,
        String singleMinutes
) {
    /** Returns the 24-character flat string representation. */
    public String toFlatString() {
        return seconds + fiveHours + singleHours + fiveMinutes + singleMinutes;
    }
}
