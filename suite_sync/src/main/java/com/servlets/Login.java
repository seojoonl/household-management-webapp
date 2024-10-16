package com.servlets;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.UserDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.models.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDao userDAO = new UserDao();

    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserDao dao = new UserDao();
		if(dao.validateUser(username, password)) {
			HttpSession session=request.getSession();
			session.setAttribute("username",username);
			response.sendRedirect("home.html"); 
		}
		else {
			response.sendRedirect("login.html");
		}
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonData = jsonBuilder.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        String username = jsonObject.get("username").getAsString(); // Now get the username
        String password = jsonObject.get("password").getAsString();
        User user = userDAO.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
        	 HttpSession session = request.getSession();
             session.setAttribute("userID", user.getId());
             if(userDAO.inHousehold(user.getId())) {
            	 session.setAttribute("householdID", userDAO.getHouseholdIDbyUser(user.getId()));
            	 response.getWriter().write("{\"success\": true, \"redirect\": \"household.html\"}");
             }
             else {
                 response.getWriter().write("{\"success\": true, \"redirect\": \"joinHouseholdPage.html\"}");
             }
         } else {
             request.setAttribute("loginError", "Invalid username or password.");
             response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password.\"}");
         }
        }
}
