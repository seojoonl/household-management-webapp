package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.models.Task;
import com.config.MyConnection;

public class TaskDao {

	public String addTask(String tname, int hid) {
		try (Connection conn = MyConnection.getConnection()) {
        try (PreparedStatement pst = conn.prepareStatement("INSERT INTO Tasks (TaskName, Completed, HID) VALUES (?, ?, ?)")) {
            pst.setString(1, tname);
            pst.setBoolean(2, false);
            pst.setInt(3, hid);
            pst.executeUpdate();
            return "Task Created";
        }} catch (SQLException e) {
            e.printStackTrace();
            return "Task Create Error: " + e.getMessage();
        }
    }
	

	public Task getTask(int tid) {
		try(Connection conn = MyConnection.getConnection()){
        try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM Tasks WHERE TID=?")) {
            pst.setInt(1, tid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Task task = new Task(tid, rs.getString("TaskName"));
                task.setCompleted(rs.getBoolean("Completed"));
                return task;
            }
            return null;
        }} catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
		
	
	public int getTaskID(String name) {
		int tid = -1;
		try(Connection conn = MyConnection.getConnection()){
        try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM Tasks WHERE TaskName=?")) {
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tid = rs.getInt("TID");
                return tid;
            }
            return -1;
        }} catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
	
	 public String toggleTaskCompletion(int tid) {
		 try(Connection conn = MyConnection.getConnection()){
	        try (PreparedStatement pst = conn.prepareStatement("UPDATE Tasks SET Completed = NOT Completed WHERE TID=?")) {
	            pst.setInt(1, tid);
	            int affectedRows = pst.executeUpdate();
	            if(affectedRows == 1) {
	            	return "Task completed";
	            }else {
	            	return "Task not found";
	            }
	        }} catch (SQLException e) {
	            return "Error toggling task completion: " + e.getMessage();
	        }
	    }
	 
	 public String removeTask(int tid) {
		 try(Connection conn = MyConnection.getConnection()){
	        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM Tasks WHERE TID=?)")) {
	            pst.setInt(1, tid);
	            pst.executeUpdate();
	            return "Task Deleted";
	        }} catch (SQLException e) {
	            e.printStackTrace();
	            return "Task Deletion Error: " + e.getMessage();
	        }
	    }
	 
	 public boolean removeTask(int hid, String taskName) {
		 try(Connection conn = MyConnection.getConnection()){
	        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM Tasks WHERE HID=? AND TaskName=?")) {
	            pst.setInt(1, hid);
	            pst.setString(2,  taskName);
	            pst.executeUpdate();
	            return true;
	        }} catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 
	 
	 public String assignTaskToUser(int uid, int tid, int hid) {
		try(Connection conn = MyConnection.getConnection()) {
			 PreparedStatement pst = conn.prepareStatement("UPDATE Tasks SET UID=? WHERE TID=?");
			 pst.setInt(1, uid);
			 pst.setInt(2, tid);
			 pst.executeUpdate();
			 return "Assigned Task to User";
		} catch (SQLException e) {
			 e.printStackTrace();
			 return "Could not assign task: " + e.getMessage();
		}
	 }
	 
	 public List<Task> getTasksByHousehold(int hid) {
		List<Task> tasks = new ArrayList<Task>();
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM Tasks WHERE HID = ?");
			pst.setString(1, Integer.toString(hid));
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int tid = rs.getInt("TID");
				Task t = getTask(tid);
				tasks.add(t);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	 }

 public double getCompletionPercentage(int hid) {
		    String query = "SELECT COUNT(*) AS total_tasks, SUM(CASE WHEN Completed = 1 THEN 1 ELSE 0 END) AS completed_tasks FROM tasks WHERE HID = ?";
		    try(Connection conn = MyConnection.getConnection()){
		    try (PreparedStatement pst = conn.prepareStatement(query)) {        
		        pst.setInt(1, hid); 
		        try (ResultSet rs = pst.executeQuery()) {
		            if (rs.next()) {
		                int totalTasks = rs.getInt("total_tasks");
		                int completedTasks = rs.getInt("completed_tasks");
		                if (totalTasks > 0) {
		                    return (double) completedTasks / totalTasks * 100;
		                }
		            }
		        }
		    }} catch (SQLException e) {
		        System.err.println("Error accessing the database: " + e.getMessage());
		    }
		    return 0; // Return 0 if there's an error, no data, or no tasks
		}
 public int getTaskUID(int tid) {
	 int uid = -1;
	 try(Connection conn = MyConnection.getConnection()){
     try (PreparedStatement pst = conn.prepareStatement("SELECT UID FROM Tasks WHERE TID=?")) {
         pst.setInt(1, tid);
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
             uid = rs.getInt("UID");
             return uid;
         }
         return -1;
     }} catch (SQLException e) {
         e.printStackTrace();
         return -1;
     }
 }
}
