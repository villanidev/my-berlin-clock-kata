interface Props {
  berlinInput: string;
  onBerlinInputChange: (v: string) => void;
  onSubmit: (e: React.FormEvent) => Promise<void>;
  error: string | null;
  result: string | null;
}

export default function ToDigitalForm({
  berlinInput,
  onBerlinInputChange,
  onSubmit,
  error,
  result,
}: Props) {
  return (
    <div className="converter-panel">
      <h2>Berlin Clock → Digital</h2>

      <form onSubmit={onSubmit}>
        <input
          className="panel-input"
          type="text"
          placeholder="24-char string, e.g. YOOOOOOOOOOOOOOOOOOOOOOO"
          value={berlinInput}
          onChange={(e) => onBerlinInputChange(e.target.value.toUpperCase())}
          maxLength={24}
        />
        {error && <p className="error">{error}</p>}
        <button type="submit" style={{ width: "100%" }}>
          Convert
        </button>
      </form>

      {result && (
        <>
          <div className="digital-time">{result}</div>
          <p className="hint">
            Seconds show parity only (0 = even, 1 = odd) — the Berlin Clock
            encodes whether seconds are even or odd, not the exact value.
          </p>
        </>
      )}
    </div>
  );
}
