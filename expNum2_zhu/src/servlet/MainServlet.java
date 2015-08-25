package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

import db.DB;

public class MainServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("UTF-8");
		String username = req.getParameter("username");
		String pwd = req.getParameter("pwd");

		DB db = new DB();
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			user = db.CheckUser(username, pwd);
		}
		session.setAttribute("user", user);
		if (user != null) {
			ArrayList al = db.findLyInfo();
			session.setAttribute("al", al);
			res.sendRedirect("main.jsp");
		} else {
			res.sendRedirect("login.jsp");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}
}
