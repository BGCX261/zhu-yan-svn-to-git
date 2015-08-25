package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class CheckUser extends HttpServlet {

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		arg1.setContentType("text/html");
		PrintWriter out =arg1.getWriter();
		String [] xhs ={"081110","081112","081113","081114"};
		String xh = arg0.getParameter("xh");
		String responseContext = "true";
		for(int i=0;i<xhs.length;i++){
			if(xh.equals(xhs[i])){
				responseContext="false";
			}
		}
		out.println(responseContext);
		out.flush();
		out.close();
	}
	
}
