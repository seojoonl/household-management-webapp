package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.HouseholdDao;
import com.dao.UserDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.models.User;

@WebServlet("/JoinHousehold")
public class JoinHousehold extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public JoinHousehold() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			HouseholdDao hdao = new HouseholdDao();
			UserDao udao = new UserDao();
			StringBuilder jsonBuilder = new StringBuilder();
	        String line;
	        while ((line = request.getReader().readLine()) != null) {
	            jsonBuilder.append(line);
	        }
	        String jsonData = jsonBuilder.toString();
	        JsonObject requestJson = JsonParser.parseString(jsonData).getAsJsonObject();
			HttpSession session = request.getSession();
			int uid = (Integer) session.getAttribute("userID");
			int householdId = requestJson.get("householdId").getAsInt();
			User user = udao.getUser(uid);
			hdao.joinHousehold(hdao.getHousehold(householdId), user);
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("redirect", "household.html");
			responseJson.addProperty("status", "success");
			session.setAttribute("householdID", householdId);
			response.getWriter().write(responseJson.toString());
		}
		catch(Exception e){
			JsonObject errorJson = new JsonObject();
			errorJson.addProperty("status", "error");
			errorJson.addProperty("message", "Error joining household");
			response.getWriter().write(errorJson.toString());
		}
		
	}

}
