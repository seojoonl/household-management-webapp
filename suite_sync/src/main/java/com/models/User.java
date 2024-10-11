package com.models;


import java.util.ArrayList;
import java.util.List;


public class User {
	private int Id;
	private String username;
	private String name;
	private String password;
	private int householdID = -1;
	List<Task> userTaskList = new ArrayList<Task>();
	public User(String username, String password, String name) {
		this.username = username;
		this.password = password;
		this.name = name;
	}
	
	void joinHouseHold(int householdID) {
		this.setHouseholdID(householdID);
	}
	@SuppressWarnings("unused")
	void createHousehold() {
		Household house = new Household();
	
	}
	void checkoffTask(Task task) {
		//For now just remove it
		userTaskList.remove(task);
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getHouseholdID() {
		return householdID;
	}
	public void setHouseholdID(int householdID) {
		this.householdID = householdID;
	}
	public void setUserTaskList(List<Task> userTasks) {
		userTaskList = userTasks;
	}

	public void addTask(Task task) {
		userTaskList.add(task);
	}

	public void clearTasks() {
		userTaskList.clear();
	}
	
	public List<Task> getUserTasks() {
		return this.userTaskList;
	}

}
