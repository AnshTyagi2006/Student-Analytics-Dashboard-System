🎓 Student Analytics Web App

A full-stack Student Analytics Dashboard built using pure Java (no frameworks) for the backend and a simple HTML/CSS/JS frontend.
This project demonstrates REST API design, file-based persistence, and frontend-backend integration.

🚀 Features

✅ Add new students

📋 View all students

🔍 Search by name or department

📊 View analytics & statistics

💾 Persistent storage using JSON (no database required)

🌐 REST API with CORS support

⚡ Lightweight (Java Standard Library only)

🛠️ Tech Stack

Backend

Java (Core Java, HttpServer)

JSON File Storage

Frontend

HTML5

CSS3

JavaScript (Fetch API)

📁 Project Structure
StudentWebApp/
│
├── backend/
│   ├── WebServer.java
│   ├── StudentsHandler.java
│   ├── SearchHandler.java
│   ├── StatsHandler.java
│   └── ...
│
├── frontend/
│   ├── index.html
│   ├── styles.css
│   └── script.js
│
└── data/
    └── students.json
⚙️ Setup & Run
1️⃣ Clone the Repository
git clone https://github.com/your-username/student-analytics-web-app.git
cd student-analytics-web-app
2️⃣ Run Backend Server
cd StudentWebApp/backend
javac *.java
java WebServer

Server will start at:
👉 http://localhost:8080

3️⃣ Open Frontend

Option 1 (Recommended):

Open StudentWebApp/frontend/index.html in browser

Option 2 (via backend server):

http://localhost:8080/../frontend/index.html
🔌 API Endpoints
Method	Endpoint	Description
GET	/api/students	Get all students
POST	/api/students	Add a new student
GET	/api/search	Search by name/department
GET	/api/stats	Get analytics/statistics
📊 Example Workflow

➕ Add students using the form

📋 View students in dashboard

🔍 Search by name or department

📊 Check statistics

🔁 Restart server → Data remains saved

💾 Data Persistence

Data is stored in:

StudentWebApp/data/students.json

Ensures no data loss after server restart

🧠 Key Learning Concepts

REST API development using Java

Handling HTTP requests manually

File-based JSON persistence

Frontend-backend communication (Fetch API)

Basic analytics computation

⚠️ Notes

Make sure port 8080 is free before running

CORS is enabled for smooth frontend integration

No external libraries are used

🌟 Future Improvements

🔐 Authentication system

🗄️ Database integration (MySQL / MongoDB)

📈 Advanced analytics & charts

🎨 Improved UI/UX (React or Tailwind)

☁️ Deployment (AWS / Render)

👨‍💻 Author

Ansh
BTech AI & DS Student

⭐ Support

If you like this project:

👉 Give it a ⭐ on GitHub
👉 Share with your friends
