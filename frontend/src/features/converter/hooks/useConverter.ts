import { useState } from "react";
import type { BerlinClockResponse } from "../../berlin-clock/types/BerlinClockTypes";
import {
  fetchToBerlin,
  fetchToDigital,
} from "../../berlin-clock/services/berlinClockService";

export interface ConverterViewModel {
  digitalTime: string;
  setDigitalTime: (v: string) => void;
  berlinInput: string;
  setBerlinInput: (v: string) => void;
  clockResult: BerlinClockResponse | null;
  timeResult: string | null;
  error: string | null;
  convertToBerlin: (e: React.FormEvent) => Promise<void>;
  convertToDigital: (e: React.FormEvent) => Promise<void>;
}

export function useConverter(): ConverterViewModel {
  const [digitalTime, setDigitalTime] = useState<string>("12:00:00");
  const [berlinInput, setBerlinInput] = useState<string>("");
  const [clockResult, setClockResult] = useState<BerlinClockResponse | null>(
    null,
  );
  const [timeResult, setTimeResult] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function convertToBerlin(e: React.FormEvent): Promise<void> {
    e.preventDefault();
    setError(null);
    setClockResult(null);
    try {
      const data = await fetchToBerlin(digitalTime);
      setClockResult(data);
    } catch (err) {
      setError((err as Error).message);
    }
  }

  async function convertToDigital(e: React.FormEvent): Promise<void> {
    e.preventDefault();
    setError(null);
    setTimeResult(null);
    try {
      const data = await fetchToDigital(berlinInput);
      setTimeResult(data.time);
    } catch (err) {
      setError((err as Error).message);
    }
  }

  return {
    digitalTime,
    setDigitalTime,
    berlinInput,
    setBerlinInput,
    clockResult,
    timeResult,
    error,
    convertToBerlin,
    convertToDigital,
  };
}
