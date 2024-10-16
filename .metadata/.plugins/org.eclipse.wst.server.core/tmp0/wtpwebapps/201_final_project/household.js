document.addEventListener('DOMContentLoaded', updateProgressBar());
function populateHome() {
	fetch("/201_final_project/HouseholdPage", {
		method: "POST",
		headers: { "Content-Type": "application/json" }
	})
		.then((response) => response.json())
		.then((data) => {
			console.log(data);
			document.getElementById("name").innerHTML = data.hname;
			document.getElementById("hid").innerHTML = "Household ID: " + data.hid;
			document.getElementById("memberlist").innerHTML = "Members: " + data.members.join(", ");
			const progressPercentage = parseFloat(data.progress).toFixed(2);
			document.getElementById("progress").innerHTML = "Progress: " + progressPercentage + "%";

			document.getElementById("tasks").innerHTML = "";

			const tasksContainer = document.getElementById("tasks");
			data.taskassignments.forEach((assignment) => {
				const taskName = Object.keys(assignment)[0];
				const assignedMember = assignment[taskName];
				const isCompleted = assignment.isCompleted; // Assumed to be provided by your backend

				const taskElement = document.createElement("div");
				taskElement.classList.add("task");

				const taskNameElement = document.createElement("span");
				taskNameElement.textContent = taskName;
				taskElement.appendChild(taskNameElement);

				const assignedMemberElement = document.createElement("span");
				assignedMemberElement.textContent = "Assigned to: " + assignedMember;
				taskElement.appendChild(assignedMemberElement);

				const deleteButton = document.createElement("button");
				deleteButton.textContent = "Delete";
				deleteButton.onclick = () => deleteTask(taskName);
				taskElement.appendChild(deleteButton);

				const completeButton = document.createElement("button");
				completeButton.textContent = isCompleted ? "Uncomplete" : "Complete";
				completeButton.onclick = isCompleted ? () => uncompleteTask(taskName) : () => completeTask(taskName);
				taskElement.appendChild(completeButton);

				tasksContainer.appendChild(taskElement);
			});
		})
		.catch(console.error);
}

function completeTask(taskName) {
	console.log("Completing task:", taskName);
	fetch("/201_final_project/CompleteTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ taskName: taskName, status: 'complete' })
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				console.log("Task marked as completed.");
				updateProgressBar();
				populateHome();  // Refresh the list to show the updated button
			} else {
				alert("Error completing task");
			}
		})
		.catch(console.error);
}

function uncompleteTask(taskName) {
	console.log("Uncompleting task:", taskName);
	fetch("/201_final_project/CompleteTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ taskName: taskName, status: 'uncomplete' })
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				updateProgressBar();
				console.log("Task marked as uncompleted.");
				populateHome();  // Refresh the list to show the updated button
			} else {
				alert("Error uncompleting task");
			}
		})
		.catch(console.error);
}

function deleteTask(taskId) {
	fetch("/201_final_project/DeleteTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ tid: taskId })
	})
		.then((response) => response.json())
		.then((data) => {
			console.log(data);
			if (data.status === "success") {
				populateHome();
			} else {
				alert(data.message);
			}
		})
		.catch(console.error);

	fetch("/201_final_project/DeleteTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ tid: taskId })
	})
		.then((response) => response.json())
		.then((data) => {
			if (data.status === "success") {
				populateHome();
			} else {
				alert("Error deleting task");
			}
		})
		.catch(console.error);
}

function openAddTaskPopup() {
	document.getElementById("addTaskPopup").style.display = "block";
}

function closePopup() {
	document.getElementById("addTaskPopup").style.display = "none";
}

function addTask() {
	const taskName = document.getElementById("taskName").value;
	if (taskName.trim() !== "") {
		fetch("/201_final_project/CreateTask", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ task: taskName })
		})
			.then((response) => response.json())
			.then((data) => {
				console.log(data);
				if (data.status === "success") {
					closePopup();
					populateHome();
				} else {
					alert("Error adding task");
				}
			})
			.catch(console.error);
	} else {
		alert("Please enter a task name");
	}
}


function openCompleteTaskPopup() {
	document.getElementById("completeTaskPopup").style.display = "block";
}

/*
function completeTask(taskName) {
	console.log(taskName);

	fetch("/201_final_project/CompleteTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ taskName: taskName })
	})
		.then((response) => response.json())
		.then((data) => {
			console.log(data);
			if (data.status === "success") {
				alert("Task Completed!");
				updateProgressBar();
				populateHome();
			} else {
				alert("Error completing task");
			}
		})
		.catch(console.error);
}
*/

function updateProgressBar() {
	fetch("/201_final_project/ProgressBar", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
	})
		.then(response => response.json())
		.then(data => {
			console.log("Received percentage:", data.percentage);
			var progressBar = document.getElementById("progress");
			console.log("Current innerText before update:", progressBar.innerText);

			progressBar.style.width = data.percentage + '%';
			progressBar.innerText = data.percentage.toFixed(2) + "% Complete";

			if (data.percentage > 75) {
				progressBar.style.backgroundColor = 'green';
			} else if (data.percentage > 50) {
				progressBar.style.backgroundColor = 'orange';
			} else {
				progressBar.style.backgroundColor = 'red';
			}

			console.log("Updated innerText after:", progressBar.innerText);
			if (data.status === "success") {
				console.log("Progress bar updated to " + data.percentage + "%");
			} else {
				alert("Error: " + data.message);
			}
		})
		.catch(error => {
			console.error(error);
			alert("Failed to update progress bar. See console for details.");
		});
}


function shuffleTasks() {
	fetch("/201_final_project/ShuffleTask", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
	})
		.then((response) => response.json())
		.then((data) => {
			console.log(data);
			if (data.status === "success") {
				alert("Shuffle Completed!");
				populateHome();
			} else {
				alert("Error: " + data.message);
			}
		})
		.catch(error => {
			console.error(error);
			alert("Failed to update progress bar. See console for details.");
		});
}

document.addEventListener('DOMContentLoaded', function() {
	const logoutButton = document.getElementById('logout');

	if (logoutButton) {
		logoutButton.addEventListener('click', function(e) {
			e.preventDefault();
			localStorage.setItem('isLoggedIn', 'false');
			window.location.href = 'index.html';
		});
	}

});