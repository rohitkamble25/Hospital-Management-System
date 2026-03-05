const BASE_URL = "https://hospital-management-system-vlze.onrender.com/api";

async function registerUser() {
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const error = document.getElementById("error");
    const success = document.getElementById("success");

    error.innerText = "";
    success.innerText = "";

    if(!name || !email || !password) {
        error.innerText = "⚠ Please fill all fields";
        return;
    }
    try{
        const res = await fetch(`${BASE_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name,
                email,
                password
            })
        });

        if (res.ok) {
            success.innerText = "✔ Registration successful! Redirecting...";
            setTimeout(() => {
                window.location.href = "login.html";
            }, 1500);
        } else {
            const msg = await res.text();
            error.innerText = msg || "Registration failed";
        }
    } catch {
        error.innerText = "Server error. Try again later.";
    }
}
