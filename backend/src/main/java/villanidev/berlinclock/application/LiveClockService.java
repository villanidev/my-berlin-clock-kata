package villanidev.berlinclock.application;

/**
 * Abstraction for real-time Berlin Clock broadcasting.
 *
 * <p>Implementations define the transport mechanism (SSE, WebSocket, Long Polling).
 * The controller depends only on this contract.
 */
public interface LiveClockService {

    /**
     * Registers a new subscriber and returns a transport-specific handle.
     *
     * @return a handle appropriate to the underlying transport (e.g. SseEmitter)
     */
    Object subscribe();

    /**
     * Broadcasts the current Berlin Clock state to all active subscribers.
     */
    void tick();

    /**
     * Sends a keep-alive signal to prevent proxy timeouts.
     * Default is no-op — only meaningful for persistent connection transports.
     */
    default void heartbeat() {}

    /**
     * Returns the number of active subscribers.
     */
    int activeCount();
}
