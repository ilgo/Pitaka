package zen.ilgo.pitaka.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import zen.ilgo.pitaka.server.Activator;
import zen.ilgo.pitaka.db.Session;

public class Page extends HttpServlet {

	private static final long serialVersionUID = 8148909500798704155L;
	private static String id = Content.class.toString();

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		ILog log = Activator.getDefault().getLog();
		String resourceId = null;
		String page = null;
		IStatus status = null;
		try {
			resourceId = request.getParameter("id");
			page = request.getParameter("page");

			response.setContentType("text/html");
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.println(queryPageData(resourceId, page));
			writer.close();

			status = new Status(IStatus.INFO, id,
					"Page Servlet: " + resourceId, null);

		} catch (Exception e) {
			status = new Status(IStatus.ERROR, id, "Page Servlet: "
					+ resourceId, e);

		} finally {
			log.log(status);
		}
	}

	private String queryPageData(String id, String page) throws XMLDBException, IOException {

		Session session = Session.getInstance();
		Collection col = session.getRootCollection();
		XPathQueryService service = session.getQueryService(col);
		InputStream is = session
				.getResource("resources/queries/yinshun.page.xq");

		BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		int read;
		while ((read = bis.read(buffer)) != -1) {
				sb.append(buffer, 0, read);
			}
		String rawQuery = sb.toString();
		int pageNr = Integer.valueOf(page);
		String prev;
		if (pageNr == 1) {
			prev = "1";
		} else {
			prev = Integer.toString(pageNr - 1);
		}
		String next = Integer.toString(pageNr + 1);

		String query = String.format(rawQuery, id, page, next);
		sb = new StringBuilder();
		ResourceSet resultSet = service.query(query);
		ResourceIterator i = resultSet.getIterator();
		while (i.hasMoreResources()) {
			Resource r = i.nextResource();
			sb.append((String) r.getContent());
			sb.append("\n");
		}
		String rawPage = sb.toString();
		Object[] args = new String[] { id, prev, id, id, next };
		String navi = String.format(navigation, args);
		return rawPage.replaceAll("NAVIGATION", navi);

	}

	private String navigation = "<table type=\"navi\"><tr>\n"
			+ "<td><a type=\"navi\" href=\"http://localhost:8080/pitaka/page?id=%s&page=%s\">Previous Page</a></td>\n"
			+ "<td><a type=\"navi\" href=\"http://localhost:8080/pitaka/content?id=%s\">Contents</a></td>\n"
			+ "<td><a type=\"navi\" href=\"http://localhost:8080/pitaka/page?id=%s&page=%s\">Next Page</a></td>\n"
			+ "</tr></table>\n";
}
