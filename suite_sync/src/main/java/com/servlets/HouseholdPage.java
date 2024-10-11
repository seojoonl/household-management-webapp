package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.HouseholdDao;
import com.dao.TaskDao;
import com.dao.UserDao;
import com.models.Household;
import com.models.Task;
import com.models.User;

@WebServlet("/HouseholdPage")
public class HouseholdPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public HouseholdPage() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			    try (PrintWriter out = response.getWriter()) {
	            String action = request.getParameter("action");
	            int taskId = Integer.parseInt(request.getParameter("taskId"));

	            if ("remove".equals(action)) 
	            {
	                TaskDao taskDao = new TaskDao();
	                @SuppressWarnings("unused")
					String result = taskDao.removeTask(taskId);  
	            }else if ("complete".equals(action)) 
	            {
	                TaskDao taskDao = new TaskDao();
	                @SuppressWarnings("unused")
					String result = taskDao.toggleTaskCompletion(taskId);  
	            }
	        } catch (Exception e) {
	        	
	        }	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		StringBuilder buildResponse = new StringBuilder();
		
		HouseholdDao hdao = new HouseholdDao();
		UserDao udao = new UserDao();
		TaskDao tdao = new TaskDao();
		
		HttpSession session = request.getSession();
		int uid = (Integer) session.getAttribute("userID");
		System.out.println(uid);
		int hid = (Integer) session.getAttribute("householdID");
		
		String hname = hdao.getName(hid);
		Household household = hdao.getHousehold(hid);
		List<User> householdUsers = household.getUsers();
		
		buildResponse.append("{");
        buildResponse.append("\"hname\": \"" + hname + "\", ");
        buildResponse.append("\"hid\": " + hid + ", ");
        buildResponse.append("\"members\": [");
		
		for (int i = 0; i < householdUsers.size(); i++) {
			String uname = householdUsers.get(i).getName();
			buildResponse.append("\"" + uname + "\"");
            if (i < householdUsers.size() - 1) {
                buildResponse.append(", ");
            }
		}
		
		double completion = tdao.getCompletionPercentage(hid);
		
		buildResponse.append("], ");
        buildResponse.append("\"progress\": \"" + completion + "\", ");
        buildResponse.append("\"taskassignments\": [");
        List<Task> householdTasks = household.getTasks();
        
        for (int i = 0; i < householdTasks.size(); i++) {
        	Task t = householdTasks.get(i);
        	String tname = t.getName();
        	boolean isCompleted = t.isCompleted();
        	String uname = " ";
        	if (t.getUid() != -1) {
        		System.out.println("taskUID: " + t.getUid());
        		try {
            		uname = udao.getUser(t.getUid()).getName();
        		}catch(Exception e) {
        			System.out.println("TID: " + t.getId() + "is not assigned");
        		}

        	}
        	buildResponse.append("{\"" + tname + "\": \"" + uname + "\", \"isCompleted\": " + isCompleted + "}");
			if (i < householdTasks.size() - 1) {
				buildResponse.append(", ");
			}
        }
        buildResponse.append("]");
        buildResponse.append("}");
        
        String responseContent = buildResponse.toString();
        pw.write(responseContent);
        pw.flush();
        pw.close();
		
	}
}
