const API_BASE = "http://localhost:8080/api/reception";
const AUTH_API = "http://localhost:8080/api/auth";

function authHeader() {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
}

async function checkAuth() {
  try {
    const res = await fetch(`${AUTH_API}/me`, { headers: authHeader() });
    if (!res.ok) throw new Error();
    const user = await res.json();
    if (user.role !== "RECEPTIONIST") throw new Error();
    return true;
  } catch {
    localStorage.removeItem("token");
    window.location.replace("login.html");
    return false;
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  const ok = await checkAuth();
  if (!ok) return;

  await loadPatients();
  await loadAppointments();

  document.querySelectorAll(".nav").forEach(nav => {
    nav.addEventListener("click", () => {
      document.querySelectorAll(".nav").forEach(n => n.classList.remove("active"));
      nav.classList.add("active");
      document.querySelectorAll(".section").forEach(sec => sec.classList.remove("show"));
      const target = document.getElementById(nav.dataset.target);
      if (target) target.classList.add("show");
      if (nav.dataset.target === "bills") loadBills();
    });
  });
});

async function loadPatients() {
  const res = await fetch(`${API_BASE}/patients`, { headers: authHeader() });
  if (!res.ok) return;
  const list = await res.json();
  const table = document.getElementById("patientsTable");
  table.innerHTML = "";
  list.forEach(p => {
    table.insertAdjacentHTML("beforeend", `
      <tr>
        <td>${p.id}</td>
        <td>${p.name}</td>
        <td>${p.age}</td>
        <td>${p.phone}</td>
        <td><button onclick="deletePatient(${p.id})">Delete</button></td>
      </tr>
    `);
  });
}

async function loadAppointments() {
  const res = await fetch(`${API_BASE}/appointments`, { headers: authHeader() });
  if (!res.ok) return;
  const list = await res.json();
  const table = document.getElementById("appointmentsTable");
  table.innerHTML = "";
  list.forEach(a => {
    table.insertAdjacentHTML("beforeend", `
      <tr>
        <td>${a.id}</td>
        <td>${a.patient?.id}</td>
        <td>${a.doctor?.id}</td>
        <td>${new Date(a.appointmentTime).toLocaleString()}</td>
        <td>${a.status}</td>
        <td><button onclick="deleteAppointment(${a.id})">Delete</button></td>
      </tr>
    `);
  });
}

async function loadBills() {
  const patientId = bid.value;

  if (!patientId) {
    document.getElementById("billsTable").innerHTML = "";
    return;
  }

  const res = await fetch(`${API_BASE}/bills?patientId=${patientId}`, {
    headers: authHeader()
  });

  if (!res.ok) return;

  const list = await res.json();
  const table = document.getElementById("billsTable");
  table.innerHTML = "";

  list.forEach(b => {
    table.insertAdjacentHTML("beforeend", `
      <tr>
        <td>${b.id}</td>
        <td>${b.patient?.id}</td>
        <td>${b.amount}</td>
        <td>${b.details}</td>
        <td>${new Date(b.createdAt).toLocaleString()}</td>
        <td><button onclick="deleteBill(${b.id})">Delete</button></td>
      </tr>
    `);
  });
}

async function createPatient() {
  const name = pname.value.trim();
  const email = pemail.value.trim();
  const password = ppassword.value.trim();
  const age = page.value;
  const phone = pphone.value.trim();
  const address = paddress.value.trim();
  const history = phistory.value.trim();

  if (!name || !email || !password || !age || !phone) return alert("All fields required");

  const res = await fetch(`${API_BASE}/patients`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeader() },
    body: JSON.stringify({
      name,
      email,
      password,
      age: Number(age),
      phone,
      address,
      medicalHistory: history
    })
  });

  if (!res.ok) return alert("Patient creation failed");

  await loadPatients();

  pname.value = "";
  pemail.value = "";
  ppassword.value = "";
  page.value = "";
  pphone.value = "";
  paddress.value = "";
  phistory.value = "";
}

async function createAppointment() {
  const patientId = pid.value;
  const doctorId = did.value;
  const time = document.getElementById("time").value;

  if (!patientId || !doctorId || !time) return alert("All fields required");

  const res = await fetch(`${API_BASE}/appointments`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeader() },
    body: JSON.stringify({
      patient: { id: Number(patientId) },
      doctor: { id: Number(doctorId) },
      appointmentTime: time
    })
  });

  if (!res.ok) return alert("Appointment creation failed");

  await loadAppointments();

  pid.value = "";
  did.value = "";
  document.getElementById("time").value = "";
}

async function createBill() {
  const patientId = bid.value;
  const amt = amount.value;
  const det = details.value;

  if (!patientId || !amt) return alert("Patient ID and Amount required");

  const res = await fetch(`${API_BASE}/bills?patientId=${patientId}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeader() },
    body: JSON.stringify({ amount: Number(amt), details: det })
  });

  if (!res.ok) return alert("Bill creation failed");

  await loadBills();

  amount.value = "";
  details.value = "";
}

async function deletePatient(id) {
  if (!confirm("Delete patient?")) return;

  const res = await fetch(`${API_BASE}/patients/${id}`, {
    method: "DELETE",
    headers: authHeader()
  });

  if (!res.ok) return alert("Delete failed");

  await loadPatients();
}

async function deleteAppointment(id) {
  if (!confirm("Delete appointment?")) return;

  const res = await fetch(`${API_BASE}/appointments/${id}`, {
    method: "DELETE",
    headers: authHeader()
  });

  if (!res.ok) return alert("Delete failed");

  await loadAppointments();
}

async function deleteBill(id) {
  if (!confirm("Delete bill?")) return;

  const res = await fetch(`${API_BASE}/bills/${id}`, {
    method: "DELETE",
    headers: authHeader()
  });

  if (!res.ok) return alert("Delete failed");

  await loadBills();
}

function logout() {
  localStorage.removeItem("token");
  window.location.replace("login.html");
}
