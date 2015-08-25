package servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DB;

import model.LyTable;
import model.User;

public class AddServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("UTF-8");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		User user = (User) req.getSession().getAttribute("user");
		LyTable ly = new LyTable();
		ly.setUserId(user.getId());
		ly.setDate(new Date(System.currentTimeMillis()));
		ly.setTitle(title);
		ly.setContent(content);
		if (new DB().addInfo(ly)) {
			res.sendRedirect("success.jsp");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}
}
