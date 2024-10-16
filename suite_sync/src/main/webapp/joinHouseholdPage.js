document
  .getElementById("join-household-form")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const data = {
      householdId: document.getElementById(`householdId`).value,
    };
    fetch("/201_final_project/JoinHousehold", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.status == "success") {
          window.location.href = data.redirect; // Redirect
        } else {
          alert(data.message);
        }
      })
      .catch((error) => console.error("Error:", error));
  });
  
    document.addEventListener('DOMContentLoaded',function()
{   
    const logoutButton = document.getElementById('logout');

    if (logoutButton) {
        logoutButton.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.setItem('isLoggedIn', 'false');
            window.location.href = 'index.html';
        });
    }
    
});