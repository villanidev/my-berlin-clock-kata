package villanidev.berlinclock.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import villanidev.berlinclock.api.dto.BerlinClockResponse;
import villanidev.berlinclock.api.dto.DigitalTimeResponse;
import villanidev.berlinclock.api.dto.ToBerlinRequest;
import villanidev.berlinclock.api.dto.ToDigitalRequest;
import villanidev.berlinclock.application.BerlinClockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import villanidev.berlinclock.application.LiveClockService;
import villanidev.berlinclock.domain.BerlinClockTime;

import java.time.LocalTime;

/**
 * Thin REST adapter: validate → delegate → return.
 * No business logic, no mapping helpers, no scheduling concerns live here.
 */
@RestController
@RequestMapping("/api/v1/berlin-clock")
@Tag(name = "Berlin Clock", description = "Convert between digital time and Berlin Clock format")
public class BerlinClockController {

    private static final Logger log = LoggerFactory.getLogger(BerlinClockController.class);

    private final BerlinClockService berlinClockService;
    private final LiveClockService liveClockService;

    public BerlinClockController(
            BerlinClockService berlinClockService,
            @Qualifier("sseLiveClockService") LiveClockService liveClockService) {
        this.berlinClockService = berlinClockService;
        this.liveClockService = liveClockService;
    }

    @GetMapping("/to-berlin")
    @Operation(summary = "Convert digital time to Berlin Clock",
            description = "Converts HH:mm:ss to a structured Berlin Clock response with individual rows")
    public BerlinClockResponse toBerlin(@Valid @ModelAttribute ToBerlinRequest request) {
        log.debug("GET /to-berlin time={}", request.time());
        BerlinClockTime clock = berlinClockService.toBerlinClock(LocalTime.parse(request.time()));
        return BerlinClockResponse.from(request.time(), clock);
    }

    @GetMapping("/to-digital")
    @Operation(summary = "Convert Berlin Clock to digital time",
            description = "Parses a 24-char Berlin Clock string back to HH:mm:ss. " +
                    "Note: seconds are recovered as parity only (even → 00, odd → 01).")
    public DigitalTimeResponse toDigital(@Valid @ModelAttribute ToDigitalRequest request) {
        log.debug("GET /to-digital clock={}", request.clock());
        return new DigitalTimeResponse(berlinClockService.toDigitalTime(request.clock()));
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Live Berlin Clock (SSE)",
            description = "Streams the current Berlin Clock state every second via Server-Sent Events")
    public SseEmitter live() {
        log.debug("GET /live - new SSE subscription requested");
        return (SseEmitter) liveClockService.subscribe();
    }
}


