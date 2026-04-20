import ClockRow from "./ClockRow";

interface Props {
  label: string;
  lamps: string[];
  unit: number;
}

export default function HoursRow({ label, lamps, unit }: Props) {
  return (
    <ClockRow
      label={label}
      lamps={lamps}
      size="wide"
      titleFn={(i) => `${(i + 1) * unit}h`}
    />
  );
}
