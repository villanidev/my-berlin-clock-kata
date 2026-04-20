package villanidev.berlinclock.api.dto;

import villanidev.berlinclock.domain.BerlinClockTime;

/**
 * SSE event payload for {@code GET /api/v1/berlin-clock/live}.
 *
 * <p>Mirrors {@link BerlinClockResponse} with {@code time} instead of
 * {@code digitalTime} (kept shorter for streaming).
 */
public record LiveClockEvent(
        String time,
        String clock,
        String seconds,
        String fiveHours,
        String singleHours,
        String fiveMinutes,
        String singleMinutes
) {
    /** Maps a domain {@link BerlinClockTime} and a formatted time string to this SSE payload. */
    public static LiveClockEvent from(String time, BerlinClockTime clock) {
        return new LiveClockEvent(
                time,
                clock.toFlatString(),
                clock.seconds(),
                clock.fiveHours(),
                clock.singleHours(),
                clock.fiveMinutes(),
                clock.singleMinutes()
        );
    }
}
