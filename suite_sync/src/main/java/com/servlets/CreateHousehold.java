package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.dao.HouseholdDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/CreateHousehold")
public class CreateHousehold extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CreateHousehold() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("debug: 0");
		try {
			StringBuilder jsonBuilder = new StringBuilder();
	        String line;
	        while ((line = request.getReader().readLine()) != null) {
	            jsonBuilder.append(line);
	        }
	        String jsonData = jsonBuilder.toString();
	        JsonObject requestJson = JsonParser.parseString(jsonData).getAsJsonObject();
	        System.out.println("debug: 1");
	        HttpSession session = request.getSession();
	        int uid = (int) session.getAttribute("userID");
	        HouseholdDao dao = new HouseholdDao();
	        String householdName = requestJson.get("householdName").getAsString();
	        System.out.println("debug: 2");
	        int hid = dao.createHousehold(householdName, uid);
	        session.setAttribute("householdID", hid);
	        System.out.println("user id: " + uid);
	        System.out.println("household id of created household: " + hid);
	        session.setAttribute("householdID", hid);
	        JsonObject responseJson = new JsonObject();
	        responseJson.addProperty("status", "success");
	        responseJson.addProperty("redirect", "household.html");
	        response.getWriter().write(responseJson.toString());
		}catch(Exception e) {
			e.printStackTrace();
			JsonObject errorJson = new JsonObject();
			errorJson.addProperty("status", "error");
			errorJson.addProperty("message", "Error creating household ERROR");
			response.getWriter().write(errorJson.toString());
		}

	}

}
