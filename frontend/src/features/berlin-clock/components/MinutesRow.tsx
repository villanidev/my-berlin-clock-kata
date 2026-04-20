import ClockRow from "./ClockRow";

interface Props {
  label: string;
  lamps: string[];
  unit: number;
}

export default function MinutesRow({ label, lamps, unit }: Props) {
  return (
    <ClockRow
      label={label}
      lamps={lamps}
      size={lamps.length > 4 ? "narrow" : "wide"}
      titleFn={(i) => `${(i + 1) * unit}m`}
    />
  );
}
