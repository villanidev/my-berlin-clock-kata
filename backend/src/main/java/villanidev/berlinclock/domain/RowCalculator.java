package villanidev.berlinclock.domain;

import java.time.LocalTime;

/**
 * Strategy for computing a single Berlin Clock row (or lamp) as a string.
 * Each implementation is responsible for one row only (SRP).
 * New rows or display variants can be added without touching existing calculators (OCP).
 */
@FunctionalInterface
public interface RowCalculator {
    String calculate(LocalTime time);
}
