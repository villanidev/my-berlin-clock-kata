import { useConverter } from "../hooks/useConverter";
import BerlinClockDisplay from "../../berlin-clock/components/BerlinClockDisplay";

export default function ConverterView() {
  const vm = useConverter();

  return (
    <div>
      <h1>Berlin Clock Converter</h1>

      <div className="converter-grid">
        {/* ── Left: Digital → Berlin ─────────────────────────────────── */}
        <div className="converter-panel">
          <h2>Digital → Berlin Clock</h2>

          <form onSubmit={vm.convertToBerlin}>
            <input
              className="panel-input"
              type="time"
              step="1"
              value={vm.digitalTime}
              onChange={(e) => vm.setDigitalTime(e.target.value)}
            />
            {vm.berlinError && <p className="error">{vm.berlinError}</p>}
            <button type="submit" style={{ width: "100%" }}>
              Convert
            </button>
          </form>

          {vm.clockResult && (
            <>
              <BerlinClockDisplay clock={vm.clockResult.berlinTime} />
              <div className="result-string">{vm.clockResult.berlinTime}</div>
            </>
          )}
        </div>

        {/* ── Right: Berlin → Digital ─────────────────────────────────── */}
        <div className="converter-panel">
          <h2>Berlin Clock → Digital</h2>

          <form onSubmit={vm.convertToDigital}>
            <input
              className="panel-input"
              type="text"
              placeholder="24-char string, e.g. YOOOOOOOOOOOOOOOOOOOOOOO"
              value={vm.berlinInput}
              onChange={(e) => vm.setBerlinInput(e.target.value.toUpperCase())}
              maxLength={24}
            />
            {vm.digitalError && <p className="error">{vm.digitalError}</p>}
            <button type="submit" style={{ width: "100%" }}>
              Convert
            </button>
          </form>

          {vm.timeResult && (
            <>
              <div className="digital-time">{vm.timeResult}</div>
              <p className="hint">
                Seconds show parity only (0 = even, 1 = odd) — the Berlin Clock
                encodes whether seconds are even or odd, not the exact value.
              </p>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
