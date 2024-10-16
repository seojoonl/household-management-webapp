package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.HouseholdDao;
import com.google.gson.JsonObject;

@WebServlet("/ShuffleTask")
public class ShuffleTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShuffleTask() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HouseholdDao dao = new HouseholdDao();
		    response.setContentType("application/json");
		    PrintWriter out = response.getWriter(); 
		    HttpSession session = request.getSession();
		    try {
		        int hid = (Integer) session.getAttribute("householdID");
		        System.out.println("Shuffling task");
		        dao.shuffleTasks(hid);
		        JsonObject responseJson = new JsonObject();
		        ServerSocket.sendUpdateToAllClients("An update occurred");
		        responseJson.addProperty("status", "success");
		        out.write(responseJson.toString());
		    } catch (Exception e) {
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("status", "error");
		        errorJson.addProperty("message", "Error shuffle tasks");
		        out.write(errorJson.toString());
		    } finally {
		        out.close();  
		    }
	}

}
