package villanidev.berlinclock.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Validated query-parameter wrapper for {@code GET /to-digital}.
 * Encapsulates all input constraints so the controller remains annotation-free.
 */
public record ToDigitalRequest(
        @NotBlank(message = "clock is required")
        @Pattern(
                regexp = "[YRO]{24}",
                message = "clock must be exactly 24 characters, each Y, R, or O")
        String clock
) {}
