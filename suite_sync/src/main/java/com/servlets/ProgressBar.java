package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.TaskDao;
import com.google.gson.JsonObject;

@WebServlet("/ProgressBar")
public class ProgressBar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProgressBar() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 TaskDao dao = new TaskDao();
		    response.setContentType("application/json");
		    PrintWriter out = response.getWriter(); 
		    HttpSession session = request.getSession();
		    
		    try {
		        int hid = (Integer) session.getAttribute("householdID");
		        double percentage = dao.getCompletionPercentage(hid);
		        System.out.println(percentage);
		        JsonObject responseJson = new JsonObject();
		        responseJson.addProperty("status", "success");
		        responseJson.addProperty("percentage", percentage);  
		        out.write(responseJson.toString());
		    } catch (Exception e) {
		        JsonObject errorJson = new JsonObject();
		        errorJson.addProperty("status", "error");
		        errorJson.addProperty("message", "Error calculating completion percentage");
		        out.write(errorJson.toString());
		    } finally {
		        out.close();  
		    }
		}

	}
