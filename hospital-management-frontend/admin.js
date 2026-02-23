const API = "http://localhost:8080/api";

let doctors = [];
let patients = [];
let receptionists = [];
let currentUser = null;

let doctorModal;
let receptionistModal;

let doctorName;
let doctorSpec;
let doctorEmail;
let doctorPassword;

let receptionistName;
let receptionistEmail;
let receptionistPassword;

document.addEventListener("DOMContentLoaded", async () => {

  doctorModal = document.getElementById("doctorModal");
  receptionistModal = document.getElementById("receptionistModal");

  doctorName = document.getElementById("doctorName");
  doctorSpec = document.getElementById("doctorSpec");
  doctorEmail = document.getElementById("doctorEmail");
  doctorPassword = document.getElementById("doctorPassword");

  receptionistName = document.getElementById("receptionistName");
  receptionistEmail = document.getElementById("receptionistEmail");
  receptionistPassword = document.getElementById("receptionistPassword");

  currentUser = await checkAuth();
  if (!currentUser) return;

  document.getElementById("adminEmail").innerText = currentUser.email;

  doctorModal.classList.add("hidden");
  receptionistModal.classList.add("hidden");

  bindNav();
  await loadAll();
  showSection("dashboard");
});

function authHeader() {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
}

async function checkAuth() {

  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "login.html";
    return null;
  }

  const res = await fetch(`${API}/auth/me`, { headers: authHeader() });

  if (!res.ok) {
    window.location.href = "login.html";
    return null;
  }

  const user = await res.json();

  if (user.role !== "ADMIN") {
    window.location.href = "login.html";
    return null;
  }

  return user;
}

function bindNav() {
  document.querySelectorAll(".nav-link").forEach(l => {
    l.onclick = () => showSection(l.dataset.section);
  });

  document.getElementById("logoutBtn").onclick = logout;

  document.getElementById("openAddDoctor").onclick = () =>
    doctorModal.classList.remove("hidden");

  document.getElementById("closeDoctorModal").onclick = () =>
    doctorModal.classList.add("hidden");

  document.getElementById("saveDoctorBtn").onclick = saveDoctor;

  document.getElementById("openAddReceptionist").onclick = () =>
    receptionistModal.classList.remove("hidden");

  document.getElementById("closeReceptionistModal").onclick = () =>
    receptionistModal.classList.add("hidden");

  document.getElementById("saveReceptionistBtn").onclick = saveReceptionist;
}

async function loadAll() {
  await Promise.all([
    loadDoctors(),
    loadPatients(),
    loadReceptionists()
  ]);
  updateDashboard();
}

async function loadDoctors() {
  const r = await fetch(`${API}/admin/doctors`, { headers: authHeader() });
  doctors = r.ok ? await r.json() : [];
}

async function loadPatients() {
  const r = await fetch(`${API}/admin/patients`, { headers: authHeader() });
  patients = r.ok ? await r.json() : [];
}

async function loadReceptionists() {
  const r = await fetch(`${API}/admin/receptionists`, { headers: authHeader() });
  receptionists = r.ok ? await r.json() : [];
}

function showSection(id) {
  document.querySelectorAll(".section").forEach(s => s.classList.add("hidden"));
  document.getElementById(id).classList.remove("hidden");

  if (id === "doctors") renderDoctors();
  if (id === "receptionists") renderReceptionists();
  if (id === "patients") renderPatients();
}

function updateDashboard() {
  document.getElementById("cardDoctors").innerText = doctors.length;
  document.getElementById("cardPatients").innerText = patients.length;
}

function renderDoctors() {
  const tbody = document.querySelector("#doctorsTable tbody");
  tbody.innerHTML = doctors.map(d =>
    `<tr>
      <td>${d.id}</td>
      <td>${d.name}</td>
      <td>${d.specialization}</td>
      <td>${d.user?.email || ""}</td>
      <td>
        <button onclick="deleteDoctor(${d.id})">Delete</button>
      </td>
    </tr>`
  ).join("");
}

async function saveDoctor() {
  const res = await fetch(`${API}/admin/doctors`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeader() },
    body: JSON.stringify({
      name: doctorName.value,
      specialization: doctorSpec.value,
      email: doctorEmail.value,
      password: doctorPassword.value
    })
  });

  if (!res.ok) return alert("Doctor creation failed");

  doctorModal.classList.add("hidden");
  await loadDoctors();
  renderDoctors();
  updateDashboard();
}

async function deleteDoctor(id) {
  const res = await fetch(`${API}/admin/doctors/${id}`, {
    method: "DELETE",
    headers: authHeader()
  });

  if (!res.ok) return alert("Delete failed");

  await loadDoctors();
  renderDoctors();
  updateDashboard();
}

function renderReceptionists() {
  const tbody = document.querySelector("#receptionistsTable tbody");
  tbody.innerHTML = receptionists.map(r =>
    `<tr>
      <td>${r.id}</td>
      <td>${r.name}</td>
      <td>${r.user?.email || ""}</td>
      <td>
        <button onclick="deleteReceptionist(${r.id})">Delete</button>
      </td>
    </tr>`
  ).join("");
}

async function saveReceptionist() {
  const res = await fetch(`${API}/admin/receptionists`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeader() },
    body: JSON.stringify({
      name: receptionistName.value,
      email: receptionistEmail.value,
      password: receptionistPassword.value
    })
  });

  if (!res.ok) return alert("Receptionist creation failed");

  receptionistModal.classList.add("hidden");
  await loadReceptionists();
  renderReceptionists();
}

async function deleteReceptionist(id) {
  const res = await fetch(`${API}/admin/receptionists/${id}`, {
    method: "DELETE",
    headers: authHeader()
  });

  if (!res.ok) return alert("Delete failed");

  await loadReceptionists();
  renderReceptionists();
}

function renderPatients() {
  const tbody = document.querySelector("#patientsTable tbody");
  tbody.innerHTML = patients.map(p =>
    `<tr>
      <td>${p.id}</td>
      <td>${p.name}</td>
      <td>${p.age}</td>
      <td>${p.phone}</td>
    </tr>`
  ).join("");
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "login.html";
}
