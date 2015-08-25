package org.control;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckUser extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String[] xhs = { "081110", "081111", "081112", "081113" };
		String xh = request.getParameter("xh");
		String responseContext = "true";
		for (int i = 0; i < xhs.length; i++) {
			if (xh.equals(xhs[i]))
				responseContext = "false";
		}
		out.println(responseContext);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
