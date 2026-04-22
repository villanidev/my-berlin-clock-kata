import { API_BASE_URL } from "../../../shared/config/api";
import type {
  BerlinClockResponse,
  DigitalTimeResponse,
} from "../types/BerlinClockTypes";

async function throwApiError(res: Response): Promise<never> {
  try {
    const body = (await res.json()) as { error?: string };
    throw new Error(body.error ?? `Server error: ${res.status}`);
  } catch {
    throw new Error(`Server error: ${res.status}`);
  }
}

export async function fetchToBerlin(
  time: string,
): Promise<BerlinClockResponse> {
  const res = await fetch(
    `${API_BASE_URL}/api/v1/berlin-clock/to-berlin?time=${encodeURIComponent(time)}`,
  );
  if (!res.ok) await throwApiError(res);
  return res.json() as Promise<BerlinClockResponse>;
}

export async function fetchToDigital(
  clock: string,
): Promise<DigitalTimeResponse> {
  const res = await fetch(
    `${API_BASE_URL}/api/v1/berlin-clock/to-digital?clock=${encodeURIComponent(clock)}`,
  );
  if (!res.ok) await throwApiError(res);
  return res.json() as Promise<DigitalTimeResponse>;
}
