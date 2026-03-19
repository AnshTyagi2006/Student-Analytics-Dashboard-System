# 🎓 Student Analytics Web App

A full-stack **Student Analytics Dashboard** built using **pure Java (no frameworks)** for the backend and a simple **HTML/CSS/JS frontend**.
This project demonstrates REST API design, file-based persistence, and frontend-backend integration.

---

## 🚀 Features

* Add new students
* View all students
* Search by name or department
* View analytics & statistics
* Persistent storage using JSON (no database required)
* REST API with CORS support
* Lightweight (Java Standard Library only)

---

## 🛠️ Tech Stack

**Backend**

* Java (Core Java, HttpServer)
* JSON File Storage

**Frontend**

* HTML5
* CSS3
* JavaScript (Fetch API)

---

## 📁 Project Structure

StudentWebApp/
│
├── backend/
│   ├── WebServer.java
│   ├── StudentsHandler.java
│   ├── SearchHandler.java
│   ├── StatsHandler.java
│
├── frontend/
│   ├── index.html
│   ├── styles.css
│   ├── script.js
│
└── data/
└── students.json

---

## ⚙️ Setup & Run

### 1. Clone the Repository

git clone https://github.com/your-username/student-analytics-web-app.git
cd student-analytics-web-app

---

### 2. Run Backend Server

cd StudentWebApp/backend
javac *.java
java WebServer

Server runs at: http://localhost:8080

---

### 3. Open Frontend

Option 1:
Open StudentWebApp/frontend/index.html in browser

Option 2:
http://localhost:8080/../frontend/index.html

---

## 🔌 API Endpoints

GET /api/students → Get all students
POST /api/students → Add new student
GET /api/search → Search students
GET /api/stats → Get statistics

---

## 📊 Example Workflow

1. Add students
2. View them in dashboard
3. Search by name/department
4. Check stats
5. Restart server → data persists

---

## 💾 Data Persistence

Data is stored in:
StudentWebApp/data/students.json

No data loss after restart.

---

## 🧠 Learning Concepts

* REST API in Java
* HTTP handling without frameworks
* JSON file storage
* Frontend-backend integration
* Basic analytics logic

---

## ⚠️ Notes

* Make sure port 8080 is free
* CORS is enabled
* No external libraries used

---

## 🌟 Future Improvements

* Authentication system
* Database integration
* Advanced analytics
* Better UI (React/Tailwind)
* Deployment

---

## 👨‍💻 Author

Ansh
BTech AI & DS Student

---

## ⭐ Support

If you like this project, give it a star ⭐
