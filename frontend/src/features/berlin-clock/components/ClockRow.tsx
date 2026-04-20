import Lamp from "./Lamp";

interface Props {
  label: string;
  lamps: string[];
  size: "wide" | "narrow";
  titleFn?: (index: number) => string;
}

export default function ClockRow({ label, lamps, size, titleFn }: Props) {
  return (
    <div>
      <p className="clock-row-label">{label}</p>
      <div className="clock-row">
        {lamps.map((state, i) => (
          <Lamp key={i} state={state} size={size} title={titleFn?.(i)} />
        ))}
      </div>
    </div>
  );
}
