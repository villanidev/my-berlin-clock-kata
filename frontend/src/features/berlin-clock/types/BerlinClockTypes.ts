/** Response from GET /api/v1/berlin-clock/to-berlin */
export interface BerlinClockResponse {
  digitalTime: string;
  berlinTime: string;
  seconds: string;
  fiveHours: string;
  singleHours: string;
  fiveMinutes: string;
  singleMinutes: string;
}

/** Response from GET /api/v1/berlin-clock/to-digital */
export interface DigitalTimeResponse {
  time: string;
}

/** SSE event payload from GET /api/v1/berlin-clock/live */
export interface LiveClockEvent {
  time: string;
  clock: string;
  seconds: string;
  fiveHours: string;
  singleHours: string;
  fiveMinutes: string;
  singleMinutes: string;
}
