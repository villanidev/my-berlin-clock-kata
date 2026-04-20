package villanidev.berlinclock.application;

import villanidev.berlinclock.domain.BerlinClockTime;

import java.time.LocalTime;

/**
 * Application-layer contract for Berlin Clock conversions.
 *
 * <p>Accepts and returns domain types. String I/O validation is the
 * responsibility of the API layer (controller).
 */
public interface BerlinClockService {

    /**
     * Converts a {@link LocalTime} to its Berlin Clock representation.
     *
     * @param time the time to convert; must not be {@code null}
     * @return an immutable {@link BerlinClockTime} domain record
     */
    BerlinClockTime toBerlinClock(LocalTime time);

    /**
     * Converts a 24-character Berlin Clock string back to digital time.
     *
     * <p><b>Known limitation:</b> The seconds lamp only encodes parity
     * (Y = even, O = odd). Exact seconds cannot be recovered; this method
     * returns {@code 00} for even and {@code 01} for odd.
     *
     * @param clock exactly 24 characters, each {@code Y}, {@code R}, or {@code O}
     * @return time string in {@code HH:mm:ss} format
     */
    String toDigitalTime(String clock);
}
