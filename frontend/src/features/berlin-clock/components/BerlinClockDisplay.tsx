/**
 * Clock string layout (24 chars):
 *   [0]     – seconds lamp
 *   [1-4]   – 5-hour row  (4 lamps, R)
 *   [5-8]   – 1-hour row  (4 lamps, R)
 *   [9-19]  – 5-min row   (11 lamps, Y/R)
 *   [20-23] – 1-min row   (4 lamps, Y)
 */
import Lamp from "./Lamp";
import HoursRow from "./HoursRow";
import MinutesRow from "./MinutesRow";

interface Props {
  clock: string;
}

export default function BerlinClockDisplay({ clock }: Props) {
  if (clock.length !== 24) return null;

  const seconds = clock[0];
  const fiveHours = clock.slice(1, 5).split("");
  const oneHours = clock.slice(5, 9).split("");
  const fiveMins = clock.slice(9, 20).split("");
  const oneMins = clock.slice(20, 24).split("");

  return (
    <div className="berlin-clock" aria-label="Berlin Clock">
      <div className="clock-row">
        <Lamp
          state={seconds}
          size="seconds"
          title="Seconds (Y = even, O = odd)"
        />
      </div>

      <HoursRow label="5 hours" lamps={fiveHours} unit={5} />
      <HoursRow label="1 hour" lamps={oneHours} unit={1} />
      <MinutesRow label="5 minutes" lamps={fiveMins} unit={5} />
      <MinutesRow label="1 minute" lamps={oneMins} unit={1} />
    </div>
  );
}
