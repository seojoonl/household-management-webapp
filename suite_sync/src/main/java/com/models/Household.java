package com.models;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Household {
	private int Id;
	private String name;
	private List<User> userList;
	private List<Task> taskList;
	private List<Task> completedTasksList;
	private List<Task> userTaskList = new ArrayList<Task>();
	
	public Household() {
		this.userList = new ArrayList<User>();
		this.taskList = new ArrayList<Task>();
		this.completedTasksList = new ArrayList<Task>();
	}

	
	public void addUser(User user) {
		userList.add(user);
	}
	public void removeUser(User user) {
		userList.remove(user);
	}
	public void addTask(Task task) {
		taskList.add(task);
	}
	public void removeTask(Task task) {
		taskList.remove(task);
	}

	public void shuffleTasks() 
	{
		 Collections.shuffle(taskList);
		 for (User user : userList)
		 {
			 user.clearTasks();
		 }
		int userCount = userList.size();
      	for (int i = 0; i < taskList.size(); i++) 
      	{
      		User user = userList.get(i % userCount);
            user.addTask(taskList.get(i));
      	}
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void completeTask(int ID) {
		Task task = userTaskList.get(ID);
		task.setCompleted(true);
		completedTasksList.add(task);
	}
	
	public List<User> getUsers() {
		return this.userList;
	}
	
	public List<Task> getTasks() {
		return this.taskList;
	}
	
	//Returns percentage of tasks that have been completed
	public float getCompletionProgress() {
		if (userTaskList.size() == 0) {
			return 0; // avoid dividing by 0
		}
		return (completedTasksList.size()/userTaskList.size());
	}
}
