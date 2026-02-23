const API = "http://localhost:8080/api";

let appointments = [];
let doctorId = null;

document.addEventListener("DOMContentLoaded", async () => {
    const user = await checkAuth();
    if (!user) return;

    doctorId = user.doctorId;

    document.getElementById("doctorName").innerText = user.email;

    await loadAppointments();
    updateDashboard();
    renderAppointments();
});

async function checkAuth() {
    try {
        const res = await fetch(`${API}/auth/me`, { headers: authHeader() });

        if (!res.ok) throw new Error();

        const user = await res.json();

        if (user.role !== "DOCTOR") throw new Error();

        if (!user.doctorId) {
            alert("Doctor profile not linked");
            return null;
        }

        return user;
    } catch {
        window.location.href = "login.html";
        return null;
    }
}

function authHeader() {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

async function loadAppointments() {
    const res = await fetch(
        `${API}/doctor/appointments?doctorId=${doctorId}`,
        { headers: authHeader() }
    );

    if (res.ok) {
        appointments = await res.json();
    }
}

function updateDashboard() {
    const today = new Date().toDateString();

    const todayCount = appointments.filter(a =>
        new Date(a.appointmentTime).toDateString() === today
    ).length;

    const uniquePatients = new Set(
        appointments.map(a => a.patient.id)
    ).size;

    document.getElementById("todayAppointments").innerText = todayCount;
    document.getElementById("totalPatients").innerText = uniquePatients;
}

function renderAppointments() {
    const tbody = document.querySelector("#appointmentsTable tbody");
    tbody.innerHTML = "";

    appointments.forEach(a => {
        tbody.innerHTML += `
            <tr>
                <td>${a.id}</td>
                <td>${a.patient.name}</td>
                <td>${new Date(a.appointmentTime).toLocaleString()}</td>
                <td>${a.status}</td>
            </tr>
        `;
    });
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}
