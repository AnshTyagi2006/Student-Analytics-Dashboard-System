const API_BASE = 'http://localhost:8080/api';
let allStudents = [];
let currentPage = 1;
const studentsPerPage = 12;
let deptChart = null;
let totalStats = null;

// Theme Toggle
function toggleTheme() {
    const body = document.body;
    const currentTheme = body.getAttribute('data-theme');
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    body.setAttribute('data-theme', newTheme);
    document.querySelector('.theme-toggle').textContent = newTheme === 'light' ? '🌙' : '☀️';
    localStorage.setItem('theme', newTheme);
}

// Load saved theme
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.body.setAttribute('data-theme', savedTheme);
    document.querySelector('.theme-toggle').textContent = savedTheme === 'light' ? '🌙' : '☀️';
});

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    
    // Auto load data for sections
    if (sectionId === 'view') loadStudents();
    if (sectionId === 'dashboard') loadStats();
    if (sectionId === 'home') loadHomeStats();
}

document.getElementById('addForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('name').value.trim();
    const dept = document.getElementById('dept').value;
    const marks = parseFloat(document.getElementById('marks').value);

    if (!name || !dept || marks < 0 || marks > 100) {
        alert('Please fill all fields correctly (marks 0-100)');
        return;
    }

    // Optimistic temp student (negative ID)
    const tempId = -Date.now();
    const newStudent = { id: tempId, name, department: dept, marks };
    allStudents.unshift(newStudent);
    if (document.getElementById('view').classList.contains('active')) {
        displayStudents(allStudents, currentPage);
    }

    try {
        const formData = new URLSearchParams();
        formData.append('name', name);
        formData.append('dept', dept);
        formData.append('marks', marks);

        const response = await fetch(`${API_BASE}/students`, {
            method: 'POST',
            body: formData,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

        if (response.ok) {
            // Refresh from server to get real ID
            await loadStudents();
            document.getElementById('addForm').reset();
            // Success feedback
            showNotification('✅ Student added & saved to database!');
        } else {
            // Rollback optimistic
            allStudents = allStudents.filter(s => s.id !== tempId);
            if (document.getElementById('view').classList.contains('active')) {
                displayStudents(allStudents, currentPage);
            }
            showNotification('❌ Server error. Student not saved.', 'error');
        }
    } catch (err) {
        // Rollback on network error
        allStudents = allStudents.filter(s => s.id !== tempId);
        if (document.getElementById('view').classList.contains('active')) {
            displayStudents(allStudents, currentPage);
        }
        showNotification('⚠️ No backend connection. Start `java WebServer` in backend/ first.', 'warning');
        console.error('Add error:', err);
    }
});

function showNotification(msg, type = 'success') {
    // Simple toast notification
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = msg;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}

async function loadStudents() {
    try {
        const response = await fetch(`${API_BASE}/students`);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        allStudents = await response.json();
        displayStudents(allStudents, currentPage);
    } catch (err) {
        console.error('Load students error:', err);
        document.getElementById('studentsGrid').innerHTML = '<p>🔌 Start backend server to load students</p>';
    }
}

// ... rest of functions unchanged (displayStudents, renderPagination, performSearch, loadStats, etc.)

function displayStudents(students, page) {
    const start = (page - 1) * studentsPerPage;
    const end = start + studentsPerPage;
    const pageStudents = students.slice(start, end);
    
    document.getElementById('studentsGrid').innerHTML = pageStudents.map(s => `
        <div class="student-card">
            <h3>${s.name}</h3>
            <div class="student-dept">${s.department}</div>
            <div class="student-marks">${s.marks.toFixed(1)}%</div>
            <div class="student-id">ID: ${s.id}</div>
        </div>
    `).join('');

    renderPagination(students.length, page);
}

function renderPagination(total, page) {
    currentPage = page;
    const totalPages = Math.ceil(total / studentsPerPage);
    let html = '';
    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="page-btn ${i === page ? 'active' : ''}" onclick="displayStudents(allStudents, ${i})">${i}</button>`;
    }
    document.getElementById('pagination').innerHTML = html;
}

function performSearch() {
    const type = document.querySelector('input[name="searchType"]:checked').value;
    const q = document.getElementById('searchQuery').value.trim().toLowerCase();
    if (!q) {
        showNotification('Enter search term');
        return;
    }
    
    fetch(`${API_BASE}/search?type=${type}&q=${encodeURIComponent(q)}`)
        .then(res => {
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            return res.json();
        })
        .then(students => {
            document.getElementById('searchResults').innerHTML = students.length ? 
                students.map(s => `
                    <div class="student-card">
                        <h3>${s.name}</h3>
                        <div class="student-dept">${s.department}</div>
                        <div class="student-marks">${s.marks.toFixed(1)}%</div>
                    </div>
                `).join('') : '<p class="no-results">No students found 😔</p>';
        })
        .catch(() => {
            document.getElementById('searchResults').innerHTML = '<p>🔌 Backend required for search</p>';
        });
}

async function loadStats() {
    try {
        const response = await fetch(`${API_BASE}/stats`);
        totalStats = await response.json();
        renderStatsCards();
        renderCharts();
    } catch (err) {
        console.error(err);
        document.getElementById('stats').innerHTML = '<p>🔌 Start backend for analytics</p>';
    }
}

function renderStatsCards() {
    const { total, depts } = totalStats;
    document.getElementById('stats').innerHTML = `
        <div class="stats-grid">
            <div class="stat-card">
                <h3>${total}</h3>
                <p>Total Students</p>
            </div>
            ${Object.entries(depts).map(([dept, count]) => `
                <div class="stat-card">
                    <h3>${count}</h3>
                    <p>${dept}</p>
                </div>
            `).join('')}
        </div>
    `;
}

function renderCharts() {
    const ctx = document.getElementById('deptChart');
    if (deptChart) deptChart.destroy();
    
    deptChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(totalStats.depts),
            datasets: [{
                data: Object.values(totalStats.depts),
                backgroundColor: ['#667eea', '#764ba2', '#f093fb', '#f56565', '#4facfe'],
                borderWidth: 0,
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom' },
                title: { display: true, text: 'Students by Department', font: { size: 18 } }
            }
        }
    });
}

async function loadHomeStats() {
    if (totalStats) {
        renderStatsCardsSmall();
    } else {
        await loadStats();
        renderStatsCardsSmall();
    }
}

function renderStatsCardsSmall() {
    if (!totalStats) return;
    document.getElementById('homeStats').innerHTML = `
        <div class="stat-preview">
            <h3>${totalStats.total}</h3>
            <p>Total Students</p>
        </div>
        <div class="stat-preview">
            <h3>${Math.max(...Object.values(totalStats.depts))}</h3>
            <p>Largest Dept</p>
        </div>
        <div class="stat-preview">
            <h3>${Object.keys(totalStats.depts).length}</h3>
            <p>Departments</p>
        </div>
    `;
}

// Init
loadHomeStats();

// Add CSS for notifications (inline)
const style = document.createElement('style');
style.textContent = `
.toast {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 16px 24px;
    border-radius: 12px;
    color: white;
    font-weight: 500;
    box-shadow: 0 20px 40px rgba(0,0,0,0.15);
    z-index: 10000;
    min-width: 300px;
    animation: slideInRight 0.3s ease;
}
.toast-success { background: #10b981; }
.toast-error { background: #ef4444; }
.toast-warning { background: #f59e0b; }
@keyframes slideInRight { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
`;
document.head.appendChild(style);

