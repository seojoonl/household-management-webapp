document.addEventListener('DOMContentLoaded', function() {
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    const loginSection = document.querySelector('header');

    if (isLoggedIn) {
		console.log("here");
        loginSection.innerHTML = `
        		<header>
			  		<div class="wiki"> SUITE-SYNC Wiki Site</div>
			        <div class="household"><a href="household.html">Household</a></div>
			        <div class="logout"><a href="index.html" id="logoutLink">Logout</a></div>
		    	</header>
				`;

        // Add event listener for logout
        document.getElementById('logoutLink').addEventListener('click', function(e) {
	        e.preventDefault();  
	        localStorage.setItem('isLoggedIn', 'false');  
	        window.location.href = 'index.html';  
        });
    } else {
    console.log(localStorage.getItem('isLoggedIn'));
        loginSection.innerHTML = `
        		 <header>
			  		<div class="wiki"> SUITE-SYNC Wiki Site</div>
			        <div class="login"><a href="login.html">Login</a></div>
			    </header>
				`;
    }
});
