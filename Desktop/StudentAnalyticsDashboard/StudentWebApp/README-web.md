# Student Analytics Web App - Full Stack

## Setup & Run

1. **Backend API with Persistence:**
   ```
   cd StudentWebApp/backend
   javac *.java
   java WebServer
   ```
   - Server: http://localhost:8080
   - API: http://localhost:8080/api/students, /search, /stats
   - Data persisted in ../data/students.json

2. **Frontend Dashboard:**
   - Open `StudentWebApp/frontend/index.html` in browser
   - Or via server root: http://localhost:8080/../frontend/index.html
   - Fully linked: Add/search/stats work with backend persistence

3. **Test Full Functionality:**
   - Add students → check View/Stats
   - Restart server → data persists
   - Search by name/dept

## Features
- ✅ Add Student (POST /api/students)
- ✅ View All Students (GET /api/students)
- ✅ Search by Name/Department (GET /api/search)
- ✅ Dashboard Stats (GET /api/stats)
- ✅ JSON file persistence (no data loss)

Pure Java stdlib, no external deps. CORS enabled for frontend.
