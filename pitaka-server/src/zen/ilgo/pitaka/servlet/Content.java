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

public class Content extends HttpServlet {

	private static final long serialVersionUID = -3680019723824785270L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		String resourceId = request.getParameter("id");
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.println("<html><body>");
			writer.println(queryContents(resourceId));
			writer.println("</body></html>");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String queryContents(String resourceId) throws XMLDBException {

		Session session = Session.getInstance();
		Collection col = session.getRootCollection();
		XPathQueryService service = session.getQueryService(col);
		InputStream is = session.getResource("resources/queries/yinshun.content.xq");
		
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
		String query = sb.toString();
		query = query.replace("DOCUMENT", resourceId);
		sb = new StringBuilder();
		ResourceSet resultSet = service.query(query);
		ResourceIterator i = resultSet.getIterator();
		while (i.hasMoreResources()) {
			Resource r = i.nextResource();
			sb.append((String) r.getContent());
			sb.append("\n");
		}

		System.out.println(sb.toString());

		return sb.toString();
	}
}
