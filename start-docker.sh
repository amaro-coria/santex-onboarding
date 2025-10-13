#!/bin/bash

# Start Docker Deployment Script
# This script starts the application using Docker Compose

echo "🐳 Starting Full-Stack Application (Docker)"
echo "============================================"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker is not installed"
    echo "Please install Docker Desktop from https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker compose &> /dev/null; then
    echo "❌ Error: Docker Compose is not installed"
    echo "Please install Docker Compose"
    exit 1
fi

# Check if Docker daemon is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker daemon is not running"
    echo "Please start Docker Desktop"
    exit 1
fi

echo "✅ Prerequisites check passed"
echo ""

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Build and start services
echo "🔨 Building and starting services..."
echo "This may take a few minutes on first run..."
echo ""

docker compose up --build

# Note: This script will run in foreground and show logs
# Press Ctrl+C to stop the services
