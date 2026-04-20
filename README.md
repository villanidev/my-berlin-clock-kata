# Berlin Clock Kata

The Berlin Clock (Mengenlehre-Uhr) is a clock that displays time using sets of lit lamps. It was created by Dieter Binninger and installed in Berlin in 1975.

## How it works

Time is encoded in five rows of lamps, read from top to bottom:

- **Seconds** — one circular lamp, lit when seconds are even
- **5-hour row** — 4 lamps, each representing 5 hours (red)
- **1-hour row** — 4 lamps, each representing 1 hour (red)
- **5-minute row** — 11 lamps, each representing 5 minutes; every third lamp is red to mark quarter-hours, the rest are yellow
- **1-minute row** — 4 lamps, each representing 1 minute (yellow)

To read the time: count the lit lamps in each row and multiply by their unit value, then add the rows together.

**Example — 13:17:01**

```
O          → seconds odd
RROO       → 2 × 5h = 10h
RRRO       → 3 × 1h = 3h  → total 13h
YYROOOOOOOO → 2 × 5m = 10m
YYOO       → 2 × 1m = 2m  → total 12m... wait, 17m = YYROO + YYOO
```

## Known limitations

The seconds lamp only encodes parity (even or odd). When converting from Berlin Clock back to digital time, the exact seconds value cannot be recovered — this implementation returns the minimum value satisfying the parity constraint: `Y → :00`, `O → :01`.

This is an inherent characteristic of the Berlin Clock display, not a bug.

## Project structure

```
my-berlin-clock-kata/
├── backend/     Spring Boot REST API
├── frontend/    React + TypeScript SPA
└── docker-compose.yml
```

## Running locally

**Prerequisites:** Docker and Docker Compose.

- This app uses SSE to display a live clock, therefore to overcome knowb issue with HTTP 1 (allowing only 6 Web browser windows), it needs to be upgraded to HTTP 2 with HTTPs

```bash
# Generate self-signed certs for local HTTPS
mkdir -p frontend/nginx/certs
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout frontend/nginx/certs/key.pem \
  -out frontend/nginx/certs/cert.pem \
  -subj "/CN=localhost"

# Start everything
docker compose up --build
```

The app is available at `https://localhost`. Accept the self-signed certificate warning in the browser.

For development without Docker, see the README files in `backend/` and `frontend/`.
