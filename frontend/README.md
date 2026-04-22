# Frontend

React + TypeScript SPA for the Berlin Clock kata.

## Stack

- React 18, TypeScript, Vite
- React Router for navigation
- Native `EventSource` for SSE (no extra libraries)

## Architecture

MVVM, organized by feature (package-by-feature):

```
src/
├── features/
│   ├── berlin-clock/
│   │   ├── components/    BerlinClockDisplay and its sub-components
│   │   ├── hooks/         useLiveClock — ViewModel for the live page
│   │   ├── pages/         LiveClockPage — route-level entry point
│   │   ├── services/      berlinClockService — API calls
│   │   ├── types/         TypeScript interfaces for API responses
│   │   └── index.ts       Barrel export
│   └── converter/
│       ├── components/    ToBerlinForm, ToDigitalForm
│       ├── hooks/         useConverter — ViewModel for the converter page
│       ├── pages/         ConverterPage — route-level entry point
│       └── index.ts
├── shared/
│   ├── components/        CopyableString — clipboard button component
│   ├── hooks/             useSSE — generic EventSource hook
│   └── config/            api.ts — base URL from VITE_API_URL
├── App.tsx
├── main.tsx
└── index.css
```

### MVVM mapping

| Layer     | Location                                             |
| --------- | ---------------------------------------------------- |
| Model     | `features/berlin-clock/types/BerlinClockTypes.ts`    |
| ViewModel | `hooks/useLiveClock.ts`, `hooks/useConverter.ts`     |
| View      | `pages/LiveClockPage.tsx`, `pages/ConverterPage.tsx` |
| Service   | `services/berlinClockService.ts`                     |

Pages are thin — they delegate state and API calls to hooks, and rendering to form components. `ConverterPage` composes `ToBerlinForm` and `ToDigitalForm`, each receiving props from `useConverter`.

### Component hierarchy

```
BerlinClockDisplay
├── Lamp (seconds)
├── HoursRow → ClockRow → Lamp (×4)
├── HoursRow → ClockRow → Lamp (×4)
├── MinutesRow → ClockRow → Lamp (×11)
└── MinutesRow → ClockRow → Lamp (×4)
```

`Lamp` is the primitive — a single styled div with a `state` prop (`Y`, `R`, or `O`). `ClockRow` adds a label and maps lamps. `HoursRow` and `MinutesRow` apply domain-specific rules (size, title format) and delegate to `ClockRow`.

`CopyableString` renders a monospace bar with an inline clipboard button; transitions to a checkmark for 1.5 s after a successful copy.

### SSE

`useSSE<T>(path, eventName)` is a generic hook that opens an `EventSource`, parses the named event as JSON, and tracks the connection state. `useLiveClock` wraps it with the Berlin Clock-specific path and event name.

## Environment

Copy `.env.example` to `.env` and set the API base URL:

```bash
cp .env.example .env
# Edit VITE_API_URL if needed
```

In the Docker setup, `VITE_API_URL` is set to an empty string — nginx proxies `/api/` to the backend, so relative URLs work.

## Running locally without Docker

```bash
cd frontend
npm install
npm run dev
```

App available at `http://localhost:5173`. Make sure the backend is running at `http://localhost:8080` (matches the default `.env.example`).

## Production build

```bash
npm run build
```

Output in `dist/`, served by nginx in the Docker image. Nginx also handles HTTP to HTTPS redirect, HTTP/2, and SSE proxy configuration.
