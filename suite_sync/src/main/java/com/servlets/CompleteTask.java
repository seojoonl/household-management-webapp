package com.servlets;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.TaskDao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@WebServlet("/CompleteTask")
public class CompleteTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CompleteTask() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject responseJson = new JsonObject(); 
		TaskDao tDAO = new TaskDao();
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            String taskName = jsonObject.get("taskName").getAsString(); 
      
            
            int taskId =  tDAO.getTaskID(taskName);
            String result = tDAO.toggleTaskCompletion(taskId);

            if (result.equals("Task completed")) {
                responseJson.addProperty("status", "success");
                responseJson.addProperty("message", result);
            } else {
                responseJson.addProperty("status", "error");
                responseJson.addProperty("message", result);
            }
        } catch (Exception e) {
            responseJson.addProperty("status", "error");
            responseJson.addProperty("message", "Error updating task status: " + e.getMessage());
        }
        ServerSocket.sendUpdateToAllClients("An update occurred");
        response.getWriter().write(responseJson.toString());
	}
}
