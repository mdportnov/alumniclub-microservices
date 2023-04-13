#!/bin/sh
docker compose \
-f ../deployment/docker-local/docker-compose.yaml \
-f ../deployment/docker-local/docker-compose.dev.yaml --project-name alumni up -d

# docker compose \
# -f docker-compose.yaml \
# -f docker-compose.dev.yaml --project-name alumni up -d