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
  berlinError: string | null;
  digitalError: string | null;
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
  const [berlinError, setBerlinError] = useState<string | null>(null);
  const [digitalError, setDigitalError] = useState<string | null>(null);

  function handleSetDigitalTime(v: string) {
    setBerlinError(null);
    setDigitalTime(v);
  }

  function handleSetBerlinInput(v: string) {
    setDigitalError(null);
    setBerlinInput(v);
  }

  async function convertToBerlin(e: React.FormEvent): Promise<void> {
    e.preventDefault();
    setBerlinError(null);
    setClockResult(null);
    if (!/^\d{2}:\d{2}:\d{2}$/.test(digitalTime)) {
      setBerlinError("Time must be in HH:mm:ss format (00:00:00 – 23:59:59)");
      return;
    }
    try {
      const data = await fetchToBerlin(digitalTime);
      setClockResult(data);
    } catch (err) {
      setBerlinError((err as Error).message);
    }
  }

  async function convertToDigital(e: React.FormEvent): Promise<void> {
    e.preventDefault();
    setDigitalError(null);
    setTimeResult(null);
    if (!/^[YRO]{24}$/.test(berlinInput)) {
      setDigitalError("Clock must be exactly 24 characters, each Y, R, or O");
      return;
    }
    try {
      const data = await fetchToDigital(berlinInput);
      setTimeResult(data.time);
    } catch (err) {
      setDigitalError((err as Error).message);
    }
  }

  return {
    digitalTime,
    setDigitalTime: handleSetDigitalTime,
    berlinInput,
    setBerlinInput: handleSetBerlinInput,
    clockResult,
    timeResult,
    berlinError,
    digitalError,
    convertToBerlin,
    convertToDigital,
  };
}
