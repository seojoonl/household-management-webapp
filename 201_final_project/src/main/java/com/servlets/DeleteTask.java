package com.servlets;

import java.io.BufferedReader;
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
import com.google.gson.JsonParser;

@WebServlet("/DeleteTask")
public class DeleteTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeleteTask() {
        super();
    }
        
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        StringBuilder bufferBuilder = new StringBuilder();
        String lineString;
        try(BufferedReader reader= request.getReader()){
        	while(((lineString = reader.readLine())!=null)) {
        		bufferBuilder.append(lineString);
        	}
        }
        try {
			JsonObject responseJson = new JsonObject();

	        HttpSession session = request.getSession();

	        String dataString = bufferBuilder.toString();
	        JsonObject jsonObject = JsonParser.parseString(dataString).getAsJsonObject();
	        
	        int hid = (Integer) session.getAttribute("householdID"); 

	        TaskDao dao = new TaskDao();
	        String taskName = jsonObject.get("tid").getAsString();
	        boolean success = dao.removeTask(hid, taskName);
	   	    
	        if(success) {
		   	    responseJson.addProperty("status","success");
		   	    out.write(responseJson.toString());
	        }
	        else {
	        	responseJson.addProperty("status","error");
	        	responseJson.addProperty("message", "Error removing task");
		   	    out.write(responseJson.toString());
	        }
	        ServerSocket.sendUpdateToAllClients("An update occurred");
        } catch(Exception e) {
			JsonObject errorJson = new JsonObject();
			errorJson.addProperty("status", "error");
			errorJson.addProperty("message", "Error deleting task");
			out.write(errorJson.toString());        
		}
        
    }

}
