package villanidev.berlinclock.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Validated query-parameter wrapper for {@code GET /to-berlin}.
 * Encapsulates all input constraints so the controller remains annotation-free.
 */
public record ToBerlinRequest(
        @NotBlank(message = "time is required")
        @Pattern(
                regexp = "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",
                message = "time must be in HH:mm:ss format (00:00:00 – 23:59:59)")
        String time
) {}
