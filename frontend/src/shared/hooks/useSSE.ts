import { useState, useEffect, useRef } from "react";
import { API_BASE_URL } from "../config/api";

export interface SSEState<T> {
  data: T | null;
  connected: boolean;
}

export function useSSE<T>(path: string, eventName: string): SSEState<T> {
  const [data, setData] = useState<T | null>(null);
  const [connected, setConnected] = useState<boolean>(false);
  const sourceRef = useRef<EventSource | null>(null);

  useEffect(() => {
    const es = new EventSource(`${API_BASE_URL}${path}`);
    sourceRef.current = es;

    es.onopen = () => setConnected(true);

    es.addEventListener(eventName, (e: MessageEvent<string>) => {
      try {
        const parsed = JSON.parse(e.data) as T;
        setData(parsed);
      } catch {
        // ignore malformed events
      }
    });

    es.onerror = () => setConnected(false);

    return () => {
      es.close();
    };
  }, [path, eventName]);

  return { data, connected };
}
