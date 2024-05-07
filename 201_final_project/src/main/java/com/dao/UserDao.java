package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.models.User;
import com.config.MyConnection;


public class UserDao {
	
	public int addUser(User user) {
		int uid = -1;
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("INSERT INTO USERS(username, password, name) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getName());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()) {
				uid = rs.getInt(1);
			}
			return uid;
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	public boolean validateUser(String username, String password) {
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE Username=? and Password=?");
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public User getUser(String username) {
		User user = null;
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE Username=?");
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("UID");
				String uname = rs.getString("Username");
				String name = rs.getString("Name");
				String password = rs.getString("Password");
				user = new User(uname, password, name);
				user.setId(id);
				user.setName(name);
			}
			return user;

		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User getUser(int id) {
		User user = null;
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE UID=?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				String uname = rs.getString("Username");
				String name = rs.getString("Name");
				String password = rs.getString("Password");
				user = new User(uname, password, name);
				user.setId(id);
				user.setName(name);
			}
			return user;

		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String setUserTask(int tid,int uid) {
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("INSERT INTO UsersTasks VALUES(?)");
			pst.setInt(1, uid);
			pst.setInt(2, tid);
			pst.executeUpdate();
			return "Task Assigned";
		}catch(SQLException e) {
			e.printStackTrace();
			return "Task Assignment Error" + e;
		}
	}
	
	public List<String> getCurrentTaskList() {
		 List<String> tasks = new ArrayList<>();
		 Connection conn = null; 
		    try {
		        String query = "SELECT * FROM UsersTasks";
		        @SuppressWarnings("null")
				PreparedStatement pst = conn.prepareStatement(query);
		        ResultSet rs = pst.executeQuery();
		        while (rs.next()) {
		            String task = rs.getString("tName");
		            tasks.add(task);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace(); 
		    } finally {
		        if (conn != null) {	
		        	try { 
		        		conn.close(); 
		        	} catch (SQLException e) {
		        		e.printStackTrace();
		        	}
		        }
		    }
		    return tasks;
	}
	
	public List<User> getUsersByHousehold(int hid) {
		List<User> users = new ArrayList<User>();
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM USERS WHERE HID = ?");
			pst.setString(1, Integer.toString(hid));
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int uid = rs.getInt("UID");
				User u = getUser(uid);
				users.add(u);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public boolean inHousehold(int uid) {
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT HID FROM Users WHERE UID = ?");
			pst.setInt(1, uid);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				int hid = rs.getInt("HID");
				HouseholdDao dao = new HouseholdDao();
				if(dao.getHousehold(hid) != null) {
					return true;
				}
			}
			return false;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public int getHouseholdIDbyUser(int uid) {
		try(Connection conn = MyConnection.getConnection()) {
			PreparedStatement pst = conn.prepareStatement("SELECT HID FROM Users WHERE UID = ?");
			pst.setInt(1, uid);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				int hid = rs.getInt("HID");
				HouseholdDao dao = new HouseholdDao();
				if(dao.getHousehold(hid) != null) {
					return hid;
				}
			}
			return -1;
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}