#!/bin/bash

# Deployment script for Santex Onboarding Application
# This script builds and deploys the application using Docker Compose

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Santex Onboarding - Deployment${NC}"
echo -e "${GREEN}========================================${NC}\n"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running${NC}"
    exit 1
fi

# Check if docker-compose.yml exists
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}Error: docker-compose.yml not found${NC}"
    exit 1
fi

# Stop existing containers
echo -e "${YELLOW}1. Stopping existing containers...${NC}"
docker-compose down || true

# Remove old volumes (optional - comment out if you want to preserve data)
# echo -e "${YELLOW}2. Removing old volumes...${NC}"
# docker volume prune -f

# Build images
echo -e "${YELLOW}2. Building Docker images...${NC}"
docker-compose build --no-cache

# Start services
echo -e "${YELLOW}3. Starting services...${NC}"
docker-compose up -d

# Wait for services to be healthy
echo -e "${YELLOW}4. Waiting for services to be ready...${NC}"
sleep 10

# Check service health
echo -e "${YELLOW}5. Checking service health...${NC}"

# Check PostgreSQL
if docker-compose ps postgres | grep -q "Up"; then
    echo -e "${GREEN}✓ PostgreSQL is running${NC}"
else
    echo -e "${RED}✗ PostgreSQL failed to start${NC}"
    exit 1
fi

# Check Backend
if docker-compose ps backend | grep -q "Up"; then
    echo -e "${GREEN}✓ Backend is running${NC}"
else
    echo -e "${RED}✗ Backend failed to start${NC}"
    exit 1
fi

# Check Frontend
if docker-compose ps frontend | grep -q "Up"; then
    echo -e "${GREEN}✓ Frontend is running${NC}"
else
    echo -e "${RED}✗ Frontend failed to start${NC}"
    exit 1
fi

# Display endpoints
echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}  Deployment Successful!${NC}"
echo -e "${GREEN}========================================${NC}\n"
echo -e "Application endpoints:"
echo -e "  Frontend:  ${YELLOW}http://localhost:3000${NC}"
echo -e "  Backend:   ${YELLOW}http://localhost:8080${NC}"
echo -e "  API Docs:  ${YELLOW}http://localhost:8080/swagger-ui.html${NC}"
echo -e "  Health:    ${YELLOW}http://localhost:8080/actuator/health${NC}"
echo -e "  Metrics:   ${YELLOW}http://localhost:8080/actuator/metrics${NC}\n"

# Show logs (optional)
echo -e "${YELLOW}To view logs, run: docker-compose logs -f${NC}"
echo -e "${YELLOW}To stop services, run: docker-compose down${NC}\n"
