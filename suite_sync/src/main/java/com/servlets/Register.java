package com.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.UserDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.models.User;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Register() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDao dao = new UserDao();
		StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonData = jsonBuilder.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        String username = jsonObject.get("username").getAsString(); // Now get the username
        String password = jsonObject.get("password").getAsString();
        String name = jsonObject.get("name").getAsString();
		try {
			User user = new User(username, password, name);
			
			if(dao.getUser(username) != null) {
				response.getWriter().write("{\"success\": false, \"message\": \"Registration failed. Username or email already exists.\"}");
			}
			else {
				int uid = dao.addUser(user);
				HttpSession session = request.getSession();
				session.setAttribute("userID", uid);
				response.getWriter().write("{\"success\": true, \"redirect\": \"joinHouseholdPage.html?success=registration\"}");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
