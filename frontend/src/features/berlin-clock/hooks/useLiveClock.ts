import { useSSE } from "../../../shared/hooks/useSSE";
import type { LiveClockEvent } from "../types/BerlinClockTypes";

export interface LiveClockViewModel {
  event: LiveClockEvent | null;
  connected: boolean;
}

export function useLiveClock(): LiveClockViewModel {
  const { data: event, connected } = useSSE<LiveClockEvent>(
    "/api/v1/berlin-clock/live",
    "tick",
  );
  return { event, connected };
}
