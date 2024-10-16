package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.models.Household;
import com.models.User;
import com.models.Task;
import com.config.MyConnection;

import java.util.Collections;
import java.util.List;

public class HouseholdDao {

	public int createHousehold(String hname, User u) {
		PreparedStatement pst = null;
		int hid = 0;
		try(Connection conn = MyConnection.getConnection()) {
			int uid = u.getId();
				pst = conn.prepareStatement("INSERT INTO Households(HouseholdName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, hname);
				pst.executeUpdate();
				ResultSet rs = pst.getGeneratedKeys();
				if(rs.next()) {
					hid = rs.getInt(1);
				}
				pst = conn.prepareStatement("UPDATE users SET HID=(?) WHERE UID=(?)");
				pst.setInt(1, hid);
				pst.setInt(2, uid);
				pst.executeUpdate();
				u.setHouseholdID(hid);
			if (pst != null) {
				pst.close();
			}
			return hid;
		}
		catch (SQLException sqle) {			
			return -1;
		}
	}
	
	public int createHousehold(String hname, int uid) {
		PreparedStatement pst = null;
		int hid = 0;
		try(Connection conn = MyConnection.getConnection()) {
				pst = conn.prepareStatement("INSERT INTO Households(HouseholdName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, hname);
				pst.executeUpdate();
				ResultSet rs = pst.getGeneratedKeys();
				if(rs.next()) {
					hid = rs.getInt(1);
				}
				pst = conn.prepareStatement("UPDATE users SET HID=(?) WHERE UID=(?)");
				pst.setInt(1, hid);
				pst.setInt(2, uid);
				pst.executeUpdate();
			if (pst != null) {
				pst.close();
			}
			return hid;
		}
		catch (SQLException sqle) {			
			return -1;
		}
	}
	
	public Household getHousehold(int hid) {
	    Household h = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try(Connection conn = MyConnection.getConnection()) {
	        pst = conn.prepareStatement("SELECT * FROM Households WHERE HID=?");
	        pst.setInt(1, hid);
	        rs = pst.executeQuery();
	        if (rs.next()) {
	            String hname = rs.getString("HouseholdName");
	            h = new Household();
	            h.setName(hname);
	            h.setId(hid);
	        } else {
	            System.out.println("Error in getHousehold. Could not find household with ID " + hid + " in database.");
	            return null;
	        }
	        rs.close();
	        pst.close();

	        pst = conn.prepareStatement("SELECT * FROM Users WHERE HID=?");
	        pst.setInt(1, hid);
	        rs = pst.executeQuery();
	        UserDao udao = new UserDao();
	        while (rs.next()) {
	            int uid = rs.getInt("UID");
	            User u = udao.getUser(uid);
	            h.addUser(u);
	        }
	        rs.close();
	        pst.close();

	        pst = conn.prepareStatement("SELECT * FROM Tasks WHERE HID=?");
	        pst.setInt(1, hid);
	        rs = pst.executeQuery();
	        TaskDao tdao = new TaskDao();
	        while (rs.next()) {
	            int tid = rs.getInt("TID");
	            Task t = tdao.getTask(tid);
	            h.addTask(t);

	            int assigneduserid = rs.getInt("UID");
	            if (assigneduserid != 0) { // check that this is right
	                User assigneduser = udao.getUser(assigneduserid); // get the user this task is assigned to
	                assigneduser.addTask(t); // add that task to that user
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pst != null) pst.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return h;
	}
	
	public Boolean joinHousehold(Household h, User u) {
		 PreparedStatement pst = null;
		    try(Connection conn = MyConnection.getConnection()) {
		        conn.setAutoCommit(false);  
		        int hid = h.getId();       
		        int uid = u.getId();       
		        pst = conn.prepareStatement("UPDATE Users SET HID = ? WHERE UID = ?;");
		        pst.setInt(1, hid);
		        pst.setInt(2, uid);
		        int result = pst.executeUpdate();

		        if (result > 0) {
		            conn.commit();  
		            u.setHouseholdID(hid); // added line
		            return true;
		        } 
		    } catch (SQLException sqle) {
		    	sqle.printStackTrace();
		    } catch(Exception e) {
		    	e.printStackTrace();
		    }
		    return false;
		}

public String addTask(Task t, Household h) {
    PreparedStatement pst = null;
    try(Connection conn = MyConnection.getConnection()) {
        int tid = t.getId();
        int hid = h.getId();  
        
        pst = conn.prepareStatement("INSERT INTO HouseholdTasks (HID, TID) VALUES (?, ?)");
        pst.setInt(1, hid);
        pst.setInt(2, tid);
        pst.executeUpdate();

        return "Added Household Task";
    } catch (SQLException sqle) {
        return "Could not add task to household: " + sqle.getMessage();
    } finally {
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception on closing resources: " + ex.getMessage());
        }
    }
}

	
	public ResultSet generateHouseholdInvite() {	
		PreparedStatement pst = null;
		try(Connection conn = MyConnection.getConnection()) {
			pst = conn.prepareStatement("SELECT object_id(Households)");
			ResultSet rs = pst.executeQuery();
			if (pst != null) {
				pst.close();
			}
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
		}
		return null;

	}
	
	public String getName(int hid) {
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    String hname = null;
	    try(Connection conn = MyConnection.getConnection()) {
	        pst = conn.prepareStatement("SELECT HouseholdName FROM households WHERE HID = ?");
	        pst.setInt(1, hid);
	        rs = pst.executeQuery();
	        if(rs.next()) {
	            hname = rs.getString("HouseholdName");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pst != null) pst.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return hname;
	}
	
	
	public void shuffleTasks(int householdId) {
        UserDao udao = new UserDao();
        TaskDao tdao = new TaskDao();
        try {
            List<User> userList = udao.getUsersByHousehold(householdId);
            for(int i = 0; i < userList.size(); i++) {
            	System.out.println(userList.get(i).getName());
            }
            List<Task> taskList = tdao.getTasksByHousehold(householdId);
            for(int i = 0; i < taskList.size(); i++) {
            	System.out.println(taskList.get(i).getId());
            }
            Collections.shuffle(taskList);
            
            
            int userCount = userList.size();
            for (int i = 0; i < taskList.size(); i++) {
                User user = userList.get(i % userCount);
                Task task = taskList.get(i);
                
                tdao.assignTaskToUser(user.getId(), task.getId(), householdId);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }

	    public void deleteHousehold(Household household) {
	        try(Connection conn = MyConnection.getConnection()) {
	            conn.setAutoCommit(false); 
	            
	            try (PreparedStatement pst1 = conn.prepareStatement("DELETE FROM UsersTasks WHERE HID = ?")) {
	                pst1.setInt(1, household.getId());
	                pst1.executeUpdate();
	            }

	            try (PreparedStatement pst2 = conn.prepareStatement("DELETE FROM Tasks WHERE HID = ?")) {
	                pst2.setInt(1, household.getId());
	                pst2.executeUpdate();
	            }

	            try (PreparedStatement pst3 = conn.prepareStatement("DELETE FROM Households WHERE HID = ?")) {
	                pst3.setInt(1, household.getId());
	                pst3.executeUpdate();
	            }
	            conn.commit();
	            conn.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }	
}
