package zen.ilgo.pitaka.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Structure extends HttpServlet {

	private static final long serialVersionUID = -7215229452100048524L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		try {
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println("<html><body>");
			writer.println("Thank you, " + username
					+ ". You are now logged into the system.");
			writer.println("</body></html>");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
