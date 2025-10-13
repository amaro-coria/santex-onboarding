# 🎉 START HERE - Santex Onboarding Project

## 👋 Welcome!

This is an **example full-stack application** with Spring Boot backend and React frontend.

---

## ⚡ Quick Start

### Option A: Docker (Easiest - 2 minutes)

```bash
./start-docker.sh
```

Then open: **http://localhost:3000**

### Option B: Local Development (Hot-reload enabled)

```bash
./start-local.sh
```

Then open: **http://localhost:5173**

---

## 🏗️ What You Have

### Backend (Spring Boot)
- ✅ Java 17 + Gradle build system
- ✅ REST API at `/api/hello`
- ✅ CORS configured for frontend
- ✅ Dockerized with multi-stage build
- ✅ Production-ready configuration

**Location:** `backend/`

### Frontend (React)
- ✅ React 18 + Vite (fast dev server)
- ✅ Axios for API calls
- ✅ Beautiful, responsive UI
- ✅ Dockerized with Nginx
- ✅ Hot-reload in development

**Location:** `frontend/`

### Docker Setup
- ✅ `docker-compose.yml` for orchestration
- ✅ Multi-stage Dockerfiles
- ✅ Networked services
- ✅ Health checks

### Helper Scripts
- ✅ `start-docker.sh` - Start with Docker
- ✅ `start-local.sh` - Start local development
- ✅ `stop-docker.sh` - Stop Docker services

---

## 🎯 Your First 5 Minutes

### Step 1: Start the Application (30 seconds)

**Choose Docker:**
```bash
./start-docker.sh
```

**Or choose Local:**
```bash
./start-local.sh
```

### Step 2: Open in Browser (10 seconds)

- **Docker:** http://localhost:3000
- **Local:** http://localhost:5173

### Step 3: Verify It Works (30 seconds)

You should see:
- 🎨 Purple gradient background
- 💬 "Hello from Spring Boot!" message
- ⏰ Current timestamp
- 🔄 Refresh button

### Step 4: Test the API (30 seconds)

```bash
curl http://localhost:8080/api/hello
```

Expected response:
```json
{
  "message": "Hello from Spring Boot!",
  "timestamp": "2025-10-13T15:30:45.123"
}
```

### Step 5: Click Refresh (10 seconds)

Click the "Refresh" button in the UI and watch the timestamp update!

---

## 📖 Learning Path

### 1. Get Familiar
- ✅ Run the application
- ✅ Test all features
- ✅ Read QUICKSTART.md
- ✅ Explore the code

### 2. Understand Architecture
- ✅ Read INSTRUCTIONS.md
- ✅ Review Spring Boot code
- ✅ Review React code
- ✅ Understand Docker setup

### 3. Make Changes
- ✅ Add new API endpoint
- ✅ Create new React component
- ✅ Modify styles
- ✅ Test changes

### 4. Add Features
- ✅ Add database (PostgreSQL)
- ✅ Implement authentication
- ✅ Add more CRUD operations
- ✅ Improve UI/UX

### 5. Production Ready
- ✅ Add comprehensive tests
- ✅ Set up CI/CD
- ✅ Deploy to production
- ✅ Monitor and maintain

---

## 🛠️ Common Tasks

### Start Development
```bash
./start-local.sh
```

### Stop Everything
```bash
# Docker
./stop-docker.sh

# Local (press Ctrl+C in terminals)
```

### View Logs
```bash
# Docker
docker compose logs -f

# Local (check terminals)
```

### Rebuild After Changes
```bash
# Docker
docker compose up --build

# Local (automatic hot-reload)
```

### Test Backend
```bash
curl http://localhost:8080/api/hello
```

### Clean Everything
```bash
# Docker
docker compose down -v
docker system prune -f

# Backend
cd backend && ./gradlew clean

# Frontend
cd frontend && rm -rf node_modules dist
```

---

## 🎨 Project Structure

```
santex-onboarding/
├── 📁 backend/              Spring Boot application
│   ├── src/main/java/       Java source code
│   ├── build.gradle         Dependencies
│   └── Dockerfile           Backend container
│
├── 📁 frontend/             React application
│   ├── src/                 React source code
│   ├── package.json         npm dependencies
│   └── Dockerfile           Frontend container
│
├── 🐳 docker-compose.yml    Orchestration
├── 📜 *.sh                  Helper scripts
└── 📚 *.md                  Documentation
```

---

## 🆘 Need Help?

### Something Not Working?

1. **Check logs:**
   ```bash
   docker compose logs -f
   ```

2. **Verify services are running:**
   ```bash
   docker compose ps
   ```

3. **Test backend directly:**
   ```bash
   curl http://localhost:8080/api/hello
   ```

4. **Check ports are available:**
   ```bash
   lsof -i :8080
   lsof -i :3000
   ```

5. **Read troubleshooting:**
   - [SETUP.md - Troubleshooting](SETUP.md#troubleshooting)
   - [ACCESS_GUIDE.md](ACCESS_GUIDE.md)

### Quick Fixes

**Port in use:**
```bash
lsof -ti:8080 | xargs kill -9
lsof -ti:3000 | xargs kill -9
```

**Docker issues:**
```bash
docker compose down -v
docker system prune -f
docker compose up --build
```

**Permission issues:**
```bash
chmod +x backend/gradlew
chmod +x *.sh
```

---

**Happy Coding! 💻**
