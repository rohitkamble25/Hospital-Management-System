const BASE_URL = "https://hospital-management-system-vlze.onrender.com/api";

async function login() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const error = document.getElementById("error");
    error.innerText = "";

    if (!email || !password) {
        error.innerText = "⚠ Please enter email and password";
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            error.innerText = "❌ Invalid email or password";
            return;
        }

        const token = await response.text();
        localStorage.setItem("token", token);

        const payloadBase64 = token.split(".")[1];
        const base64 = payloadBase64.replace(/-/g, '+').replace(/_/g, '/');
        const decodedPayload = JSON.parse(atob(base64));
        const role = decodedPayload.role;

        setTimeout(() => {
            if (role === "ADMIN") {
                window.location.href = "admin.html";
            } else if (role === "DOCTOR") {
                window.location.href = "doctor.html";
            } else if (role === "PATIENT") {
                window.location.href = "patient.html";
            } else if (role === "RECEPTIONIST") {
                window.location.href = "receptionist.html";
            } else {
                window.location.href = "homepage.html";
            }
        }, 100);

    } catch (err) {
        error.innerText = "⚠ Server error. Try again later.";
    }
}
