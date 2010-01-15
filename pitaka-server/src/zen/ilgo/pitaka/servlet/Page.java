package zen.ilgo.pitaka.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import zen.ilgo.pitaka.db.Session;

public class Page extends HttpServlet {

	private static final long serialVersionUID = 8148909500798704155L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String resourceId = request.getParameter("id");
			String page = request.getParameter("page");

			response.setContentType("text/html");
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.println(queryPageData(resourceId, page));
//			writer.println("<html><body>");
//			writer.println("<p>");
//			writer.println(resourceId);
//			writer.println("</p>");
//			writer.println("<p>");
//			writer.println(page);
//			writer.println("</p>");
//			writer.println("</body></html>");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String queryPageData(String resourceId, String page) throws XMLDBException {

		Session session = Session.getInstance();
		Collection col = session.getRootCollection();
		XPathQueryService service = session.getQueryService(col);
		InputStream is = session
				.getResource("resources/queries/yinshun.page.xq");

		BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		int read;
		try {
			while ((read = bis.read(buffer)) != -1) {
				sb.append(buffer, 0, read);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String rawQuery = sb.toString();
		int pageNr = Integer.valueOf(page);
		String prev =  Integer.toString(pageNr - 1);
		String next = Integer.toString(pageNr + 1);
		String query = String.format(rawQuery, resourceId, page, next, prev);
		sb = new StringBuilder();
		ResourceSet resultSet = service.query(query);
		ResourceIterator i = resultSet.getIterator();
		while (i.hasMoreResources()) {
			Resource r = i.nextResource();
			sb.append((String) r.getContent());
			sb.append("\n");
		}
		return sb.toString();
	}
}
