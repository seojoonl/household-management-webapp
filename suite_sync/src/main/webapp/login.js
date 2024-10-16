document
  .getElementById("login-form")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const data = {
      username: document.getElementById("username").value,
      password: document.getElementById("password").value,
    };
    fetch("/201_final_project/Login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          localStorage.setItem('isLoggedIn', 'true');
          window.location.href = data.redirect; // Redirect
        } else {
          alert(data.message);
        }
      })
      .catch((error) => console.error("Error:", error));
  });
  
  