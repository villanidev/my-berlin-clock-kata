import Lamp from "./Lamp";
import HoursRow from "./HoursRow";
import MinutesRow from "./MinutesRow";

interface Props {
  seconds: string;
  fiveHours: string;
  singleHours: string;
  fiveMinutes: string;
  singleMinutes: string;
}

export default function BerlinClockDisplay({
  seconds,
  fiveHours,
  singleHours,
  fiveMinutes,
  singleMinutes,
}: Props) {
  return (
    <div className="berlin-clock" aria-label="Berlin Clock">
      <div className="clock-row">
        <Lamp
          state={seconds}
          size="seconds"
          title="Seconds (Y = even, O = odd)"
        />
      </div>

      <HoursRow label="5 hours" lamps={fiveHours.split("")} unit={5} />
      <HoursRow label="1 hour" lamps={singleHours.split("")} unit={1} />
      <MinutesRow label="5 minutes" lamps={fiveMinutes.split("")} unit={5} />
      <MinutesRow label="1 minute" lamps={singleMinutes.split("")} unit={1} />
    </div>
  );
}
