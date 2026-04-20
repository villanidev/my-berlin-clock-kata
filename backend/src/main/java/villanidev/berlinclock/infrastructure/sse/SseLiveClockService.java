package villanidev.berlinclock.infrastructure.sse;

import villanidev.berlinclock.api.dto.LiveClockEvent;
import villanidev.berlinclock.application.BerlinClockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import villanidev.berlinclock.application.LiveClockService;

@Service
public class SseLiveClockService implements LiveClockService {

    private static final long   SSE_TIMEOUT_MS        = 5L * 60 * 1000; // 5 minutes
    private static final String SSE_EVENT_TICK        = "tick";
    private static final String SSE_HEARTBEAT_COMMENT = "heartbeat";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(SseLiveClockService.class);

    private final BerlinClockService berlinClockService;
    private final ZoneId zoneId;
    private final ObjectMapper objectMapper;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseLiveClockService(BerlinClockService berlinClockService,
                               ObjectMapper objectMapper,
                               @Value("${clock.timezone:UTC}") String timezone) {
        this.berlinClockService = berlinClockService;
        this.objectMapper = objectMapper;
        this.zoneId = ZoneId.of(timezone);
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void tick() {
        if (activeCount() == 0) return; // nobody is listening, does nothing

        try {
            LiveClockEvent payload = createLiveClockEvent();
            broadcast(
                    SseEmitter.event()
                            .id(String.valueOf(System.currentTimeMillis()))
                            .name(SSE_EVENT_TICK)
                            .data(objectMapper.writeValueAsString(payload))
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payload", e);
        }
    }

    @Override
    @Scheduled(fixedRate = 15_000)
    public void heartbeat() {
        if (activeCount() == 0) return; // nobody is listening, does nothing
        broadcast(
                SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .comment(SSE_HEARTBEAT_COMMENT)
        );
    }

    @Override
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        emitters.add(emitter);
        log.info("SSE client connected. Active subscribers: {}", activeCount());

        emitter.onCompletion(() -> remove(emitter, "completion"));
        emitter.onTimeout(()   -> remove(emitter, "timeout"));
        emitter.onError(e      -> remove(emitter, "error: " + e.getMessage()));

        return emitter;
    }

    @Override
    public int activeCount() {
        return emitters.size();
    }

    private LiveClockEvent createLiveClockEvent() {
        LocalTime now = ZonedDateTime.now(zoneId).toLocalTime();
        return LiveClockEvent.from(now.format(TIME_FORMATTER), berlinClockService.toBerlinClock(now));
    }

    public void broadcast(SseEmitter.SseEventBuilder event) {
        emitters.forEach(emitter -> {
            try {
                log.trace("Broadcasting tick to {} active emitters", activeCount());
                emitter.send(event);
            } catch (Exception e) {
                remove(emitter, "send failure");
            }
        });
    }

    private void remove(SseEmitter emitter, String reason) {
        emitters.remove(emitter);
        log.info("SSE client disconnected [{}]. Active subscribers: {}", reason, emitters.size());
    }
}
