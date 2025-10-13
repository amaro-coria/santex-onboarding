#!/bin/bash

# Stop Docker Services Script

echo "🛑 Stopping Docker services..."

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Stop and remove containers
docker compose down

echo "✅ All services stopped and containers removed"
echo ""
echo "To also remove volumes, run: docker compose down -v"
