import { useLiveClock } from "../hooks/useLiveClock";
import BerlinClockDisplay from "../components/BerlinClockDisplay";
import CopyableString from "../../../shared/components/CopyButton";

export default function LiveClockPage() {
  const { event, connected } = useLiveClock();

  return (
    <div>
      <h1>Live Berlin Clock</h1>

      <span
        className={`status-badge ${connected ? "connected" : "disconnected"}`}
      >
        {connected ? "● Live" : "○ Disconnected"}
      </span>

      {event && (
        <>
          <BerlinClockDisplay
            seconds={event.seconds}
            fiveHours={event.fiveHours}
            singleHours={event.singleHours}
            fiveMinutes={event.fiveMinutes}
            singleMinutes={event.singleMinutes}
          />
          <div className="live-time">{event.time}</div>
          <CopyableString text={event.clock} />
        </>
      )}

      {!event && !connected && (
        <p className="error">
          Unable to connect to the server. Is the backend running?
        </p>
      )}
    </div>
  );
}
