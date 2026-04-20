package villanidev.berlinclock.api.dto;

import villanidev.berlinclock.domain.BerlinClockTime;

/**
 * Response payload for {@code GET /api/v1/berlin-clock/to-berlin}.
 *
 * <p>In addition to the canonical 24-char {@code berlinTime} string, each row
 * is exposed individually so clients can render the clock without string-slicing.
 */
public record BerlinClockResponse(
        String digitalTime,
        String berlinTime,
        String seconds,
        String fiveHours,
        String singleHours,
        String fiveMinutes,
        String singleMinutes
) {
    /** Maps a domain {@link BerlinClockTime} and its originating digital-time string to this response. */
    public static BerlinClockResponse from(String digitalTime, BerlinClockTime clock) {
        return new BerlinClockResponse(
                digitalTime,
                clock.toFlatString(),
                clock.seconds(),
                clock.fiveHours(),
                clock.singleHours(),
                clock.fiveMinutes(),
                clock.singleMinutes()
        );
    }
}
