package com.models;

import com.dao.TaskDao;

public class Task {
    private int hid;
    private int id;
    private int uid;
    private String name;
    private boolean completed;  // Boolean to track whether the task is completed

    public Task(int tid, String name) {
    	//only household id since task is not assigned to specific user when created. 
        this.id = tid;
        this.name = name;
        this.completed = false;  // Default to false, task not completed
        TaskDao dao = new TaskDao();
        this.uid = dao.getTaskUID(tid);
        
    }

    public int getHid() {
        return hid;
    }
   

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}