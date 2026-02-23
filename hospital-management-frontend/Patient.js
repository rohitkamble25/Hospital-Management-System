const API_BASE = "http://localhost:8080/api";

function authHeader() {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

async function checkAuth() {
    try {
        const res = await fetch(`${API_BASE}/auth/me`, { headers: authHeader() });
        if (!res.ok) throw new Error();
        const user = await res.json();
        if (user.role !== "PATIENT") throw new Error();
        if (!user.patientId) {
            alert("Patient profile not linked");
            return null;
        }
        return user;
    } catch {
        window.location.href = "login.html";
        return null;
    }
}

document.addEventListener("DOMContentLoaded", async () => {
    const user = await checkAuth();
    if (!user) return;

    const patientId = user.patientId;

    document.querySelectorAll(".nav").forEach(link => {
        link.addEventListener("click", () => {

            if (link.id === "logoutBtn") {
                localStorage.removeItem("token");
                window.location.href = "login.html";
                return;
            }

            document.querySelectorAll(".nav").forEach(n => n.classList.remove("active"));
            link.classList.add("active");

            document.querySelectorAll(".section").forEach(sec => sec.classList.remove("show"));

            const targetId = link.dataset.target;
            const targetSection = document.getElementById(targetId);
            if (targetSection) targetSection.classList.add("show");
        });
    });

    loadAppointments(patientId);
    loadBills(patientId);
});

async function loadAppointments(patientId) {
    const res = await fetch(`${API_BASE}/patient/appointments?patientId=${patientId}`, { headers: authHeader() });
    if (!res.ok) return;
    const data = await res.json();
    const container = document.getElementById("appointmentsData");
    if (!container) return;
    container.innerHTML = "";
    if (!data.length) {
        container.innerHTML = "<p>No appointments found</p>";
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>Date & Time</th>
                    <th>Doctor</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
    `;

    data.forEach(a => {
        html += `
            <tr>
                <td>${new Date(a.appointmentTime).toLocaleString()}</td>
                <td>${a.doctor?.name || "-"}</td>
                <td>${a.status}</td>
            </tr>
        `;
    });

    html += "</tbody></table>";
    container.innerHTML = html;
}

async function loadBills(patientId) {
    const res = await fetch(`${API_BASE}/patient/bills?patientId=${patientId}`, { headers: authHeader() });
    if (!res.ok) return;
    const data = await res.json();
    const container = document.getElementById("billsData");
    if (!container) return;
    container.innerHTML = "";
    if (!data.length) {
        container.innerHTML = "<p>No bills found</p>";
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>Bill ID</th>
                    <th>Amount</th>
                    <th>Details</th>
                    <th>Created At</th>
                </tr>
            </thead>
            <tbody>
    `;

    data.forEach(b => {
        html += `
            <tr>
                <td>${b.id}</td>
                <td>₹${b.amount}</td>
                <td>${b.details || "-"}</td>
                <td>${new Date(b.createdAt).toLocaleString()}</td>
            </tr>
        `;
    });

    html += "</tbody></table>";
    container.innerHTML = html;
}
