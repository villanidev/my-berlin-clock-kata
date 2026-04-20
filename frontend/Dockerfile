# ─── Stage 1: Build ─────────────────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN npm run build

# ─── Stage 2: Serve ─────────────────────────────────────────────────────────
FROM nginx:stable-alpine
# SPA fallback: all routes serve index.html
COPY --from=build /app/dist /usr/share/nginx/html

# Certs arrive via volume in docker-compose — they are not stored in the image
# This prevents committing hidden secrets to version control and allows flexibility in cert management (e.g. using Let's Encrypt certs in production).
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
EXPOSE 443

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost/index.html || exit 1