document.addEventListener('DOMContentLoaded', function () {
  document.getElementById("signup-container").addEventListener("submit", function (event) {
    event.preventDefault();
    
    const data = {
		name : document.getElementById("name").value,
    	username : document.getElementById("username").value,
    	password : document.getElementById("password").value,
    	confirmPassword : document.getElementById("confirm_password").value,
	}


    if (data.password !== data.confirmPassword) {
        alert("Passwords do not match.");
        return; 
    }

    fetch("/201_final_project/Register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
		console.log(data);
      if (data.success) {
        localStorage.setItem('isLoggedIn', 'true');
        window.location.href = data.redirect; 
      } else {
        alert(data.message);
      }
    })
    .catch((error) => console.error("Error:", error));

  });

});
