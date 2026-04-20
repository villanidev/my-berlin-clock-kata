import { API_BASE_URL } from "../../../shared/config/api";
import type {
  BerlinClockResponse,
  DigitalTimeResponse,
} from "../types/BerlinClockTypes";

export async function fetchToBerlin(
  time: string,
): Promise<BerlinClockResponse> {
  const res = await fetch(
    `${API_BASE_URL}/api/v1/berlin-clock/to-berlin?time=${encodeURIComponent(time)}`,
  );
  if (!res.ok) throw new Error(`Server error: ${res.status}`);
  return res.json() as Promise<BerlinClockResponse>;
}

export async function fetchToDigital(
  clock: string,
): Promise<DigitalTimeResponse> {
  const res = await fetch(
    `${API_BASE_URL}/api/v1/berlin-clock/to-digital?clock=${encodeURIComponent(clock)}`,
  );
  if (!res.ok) throw new Error(`Server error: ${res.status}`);
  return res.json() as Promise<DigitalTimeResponse>;
}
