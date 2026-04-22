import BerlinClockDisplay from "../../berlin-clock/components/BerlinClockDisplay";
import CopyableString from "../../../shared/components/CopyButton";
import type { BerlinClockResponse } from "../../berlin-clock/types/BerlinClockTypes";

interface Props {
  digitalTime: string;
  onDigitalTimeChange: (v: string) => void;
  onSubmit: (e: React.FormEvent) => Promise<void>;
  error: string | null;
  result: BerlinClockResponse | null;
}

export default function ToBerlinForm({
  digitalTime,
  onDigitalTimeChange,
  onSubmit,
  error,
  result,
}: Props) {
  return (
    <div className="converter-panel">
      <h2>Digital → Berlin Clock</h2>

      <form onSubmit={onSubmit}>
        <input
          className="panel-input"
          type="time"
          step="1"
          value={digitalTime}
          onChange={(e) => onDigitalTimeChange(e.target.value)}
        />
        {error && <p className="error">{error}</p>}
        <button type="submit" style={{ width: "100%" }}>
          Convert
        </button>
      </form>

      {result && (
        <>
          <BerlinClockDisplay
            seconds={result.seconds}
            fiveHours={result.fiveHours}
            singleHours={result.singleHours}
            fiveMinutes={result.fiveMinutes}
            singleMinutes={result.singleMinutes}
          />
          <CopyableString text={result.berlinTime} />
        </>
      )}
    </div>
  );
}
