#!/bin/bash

# Start Local Development Script
# This script starts both backend and frontend for local development

echo "🚀 Starting Full-Stack Application (Local Development)"
echo "=================================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Error: Node.js is not installed"
    echo "Please install Node.js 18 or higher"
    exit 1
fi

echo "✅ Prerequisites check passed"
echo ""

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Make gradlew executable
chmod +x "$SCRIPT_DIR/backend/gradlew"

# Start backend in background
echo "🔧 Starting Spring Boot backend..."
cd "$SCRIPT_DIR/backend"
./gradlew bootRun > ../backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"
echo "Backend logs: backend.log"
echo ""

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Check if backend is running
if curl -s http://localhost:8080/api/hello > /dev/null; then
    echo "✅ Backend is running on http://localhost:8080"
else
    echo "⚠️  Backend may still be starting... Check backend.log for details"
fi
echo ""

# Install frontend dependencies if needed
cd "$SCRIPT_DIR/frontend"
if [ ! -d "node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
    echo ""
fi

# Start frontend
echo "🎨 Starting React frontend..."
echo "Frontend will be available at http://localhost:5173"
echo ""
echo "Press Ctrl+C to stop both services"
echo ""

# Start frontend (this will run in foreground)
npm run dev

# Cleanup: Kill backend when frontend stops
echo ""
echo "🛑 Stopping backend..."
kill $BACKEND_PID 2>/dev/null
echo "✅ All services stopped"
