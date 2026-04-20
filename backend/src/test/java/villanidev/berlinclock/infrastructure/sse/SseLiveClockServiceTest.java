package villanidev.berlinclock.infrastructure.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import villanidev.berlinclock.application.BerlinClockService;
import villanidev.berlinclock.domain.BerlinClockTime;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pure unit tests — no Spring context.
 * Exercises lifecycle, scheduling guards, and send-failure cleanup.
 */
@DisplayName("SseLiveClockService")
class SseLiveClockServiceTest {

    private BerlinClockService berlinClockService;
    private SseLiveClockService service;

    @BeforeEach
    void setUp() {
        berlinClockService = mock(BerlinClockService.class);
        service = new SseLiveClockService(berlinClockService, new ObjectMapper(), "UTC");
    }

    // ─── subscribe ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("subscribe")
    class Subscribe {

        @Test
        @DisplayName("returns a non-null SseEmitter")
        void returnsEmitter() {
            assertThat(service.subscribe()).isNotNull();
        }

        @Test
        @DisplayName("increments activeCount by 1")
        void incrementsActiveCount() {
            service.subscribe();
            assertThat(service.activeCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("multiple subscriptions are all tracked")
        void multipleSubscribers() {
            service.subscribe();
            service.subscribe();
            service.subscribe();
            assertThat(service.activeCount()).isEqualTo(3);
        }
    }

    // ─── activeCount ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("activeCount")
    class ActiveCount {

        @Test
        @DisplayName("starts at zero before any subscription")
        void startsAtZero() {
            assertThat(service.activeCount()).isZero();
        }

        @Test
        @DisplayName("onCompletion callback decrements count")
        void onCompletion() throws Exception {
            SseEmitter emitter = service.subscribe();
            // completionCallback is declared in the superclass ResponseBodyEmitter
            Field field = emitter.getClass().getSuperclass().getDeclaredField("completionCallback");
            field.setAccessible(true);
            ((Runnable) field.get(emitter)).run();
            assertThat(service.activeCount()).isZero();
        }

        @Test
        @DisplayName("onTimeout callback decrements count")
        void onTimeout() throws Exception {
            SseEmitter emitter = service.subscribe();
            Field field = emitter.getClass().getSuperclass().getDeclaredField("timeoutCallback");
            field.setAccessible(true);
            ((Runnable) field.get(emitter)).run();
            assertThat(service.activeCount()).isZero();
        }
    }

    // ─── broadcast cleanup ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("broadcast")
    class Broadcast {

        @Test
        @DisplayName("removes emitter when send() throws during tick")
        void tickRemovesFailedEmitter() throws Exception {
            addEmitter(mockFailingEmitter());

            stubBerlinClock();
            service.tick();

            assertThat(service.activeCount()).isZero();
        }

        @Test
        @DisplayName("removes emitter when send() throws during heartbeat")
        void heartbeatRemovesFailedEmitter() throws Exception {
            addEmitter(mockFailingEmitter());

            service.heartbeat();

            assertThat(service.activeCount()).isZero();
        }
    }

    // ─── tick ────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("tick")
    class Tick {

        @Test
        @DisplayName("does not call BerlinClockService when no subscribers")
        void noSubscribers() {
            assertThatNoException().isThrownBy(service::tick);
            verify(berlinClockService, never()).toBerlinClock(any());
        }

        @Test
        @DisplayName("calls BerlinClockService when a subscriber exists")
        void callsServiceWhenSubscribed() throws Exception {
            addEmitter(mockFailingEmitter());
            stubBerlinClock();

            service.tick();

            verify(berlinClockService).toBerlinClock(any());
        }
    }

    // ─── heartbeat ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("heartbeat")
    class Heartbeat {

        @Test
        @DisplayName("does nothing when no subscribers")
        void noSubscribers() {
            assertThatNoException().isThrownBy(service::heartbeat);
        }
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void addEmitter(SseEmitter emitter) throws Exception {
        Field f = SseLiveClockService.class.getDeclaredField("emitters");
        f.setAccessible(true);
        ((List<SseEmitter>) f.get(service)).add(emitter);
    }

    private SseEmitter mockFailingEmitter() {
        // Anonymous subclass — Java 25/GraalVM blocks Mockito from instrumenting Spring classes
        return new SseEmitter() {
            @Override
            public void send(SseEventBuilder builder) throws IOException {
                throw new IllegalStateException("simulated disconnect");
            }
        };
    }

    private void stubBerlinClock() {
        BerlinClockTime berlinTime = new BerlinClockTime("Y", "OOOO", "OOOO", "OOOOOOOOOOO", "OOOO");
        when(berlinClockService.toBerlinClock(any())).thenReturn(berlinTime);
    }
}
