import { useLiveClock } from "../hooks/useLiveClock";
import BerlinClockDisplay from "./BerlinClockDisplay";

export default function LiveClockView() {
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
          <BerlinClockDisplay clock={event.clock} />
          <div className="live-time">{event.time}</div>
          <div className="result-string">{event.clock}</div>
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
