#!/bin/bash

# Development setup script
# Sets up the local development environment

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Development Environment Setup${NC}"
echo -e "${GREEN}========================================${NC}\n"

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java installed: $(java -version 2>&1 | head -n 1)${NC}"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo -e "${RED}Error: Node.js is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Node.js installed: $(node --version)${NC}"

# Check Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker installed${NC}"

# Start PostgreSQL container
echo -e "\n${YELLOW}Starting PostgreSQL...${NC}"
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
echo -e "${YELLOW}Waiting for PostgreSQL to be ready...${NC}"
sleep 5

# Install backend dependencies
echo -e "\n${YELLOW}Installing backend dependencies...${NC}"
cd backend
./gradlew build -x test
cd ..

# Install frontend dependencies
echo -e "\n${YELLOW}Installing frontend dependencies...${NC}"
cd frontend
npm install
cd ..

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}  Setup Complete!${NC}"
echo -e "${GREEN}========================================${NC}\n"
echo -e "To start development:"
echo -e "  Backend:  ${YELLOW}cd backend && ./gradlew bootRun${NC}"
echo -e "  Frontend: ${YELLOW}cd frontend && npm run dev${NC}\n"
echo -e "PostgreSQL is running at: ${YELLOW}localhost:5432${NC}"
echo -e "Database: ${YELLOW}santex_db${NC}"
echo -e "User: ${YELLOW}santex_user${NC}\n"
