package zen.ilgo.pitaka.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import zen.ilgo.pitaka.db.Session;
import zen.ilgo.pitaka.server.JettyServer;

public class Content extends HttpServlet {

	private static final long serialVersionUID = -3680019723824785270L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {
			String resourceId = request.getParameter("id");
			String data = queryContentsData(resourceId);

			response.setContentType("text/html");
			response.setCharacterEncoding("utf8");
			PrintWriter writer = response.getWriter();
			writer.println("<html><head>");
			writer.println("<link href=\"yinshun.css\" rel=\"stylesheet\" type=\"text/css\" />");
			writer.println("</head><body>");
			writer.println(getPageBody(data, resourceId));
			writer.println("</body></html>");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String queryContentsData(String resourceId) throws XMLDBException {

		Session session = Session.getInstance();
		Collection col = session.getRootCollection();
		XPathQueryService service = session.getQueryService(col);
		InputStream is = session
				.getResource("resources/queries/yinshun.content.xq");

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
		return sb.toString();
	}

	private String getPageBody(String data, String id) {

		StringBuilder sb = new StringBuilder();
		try {
			Document doc = DocumentHelper.parseText(data);
			String title = doc.selectSingleNode("data/title").getText();
			String author = doc.selectSingleNode("data/author").getText();
			String extent = doc.selectSingleNode("data/extent").getText();
			sb.append(String.format(pageFmt, title, author, extent));
			sb.append(getContent(doc, id));

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getContent(Document doc, String id) {

		List<Element> nodes = new ArrayList<Element>();
		for (Object node : doc.selectNodes("data/pb")) {
			nodes.add((Element) node);
		}
		for (Object node : doc.selectNodes("data/div")) {
			nodes.add((Element) node);
		}
		Collections.sort(nodes, new IdComparator());

		StringBuilder sb = new StringBuilder();
		sb.append("<table>\n");
		for (int n = 0; n < nodes.size(); n++) {
			Element elem = nodes.get(n);
			if (elem.getName().equals("pb")) {
				Element div = nodes.get(++n);
				sb.append(createAnchorEntry(elem, div, id));
			} else {
				sb.append(createOrdinaryEntry(elem));
			}
			sb.append("\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	private String createAnchorEntry(Element pb, Element div, String id) {
		String page = div.attributeValue("page");
		Element a = transformDivElement(div, "a");
		a.setName("a");
		String htmPage = pb.attributeValue("page");
		String linkFmt = JettyServer.SERVER + "/page?id=%s&page=%s";
		String link = String.format(linkFmt, id, htmPage);
		a.addAttribute("href", link);
		return tableEntry(a.asXML(), page);
	}

	private String createOrdinaryEntry(Element div) {
		String page = div.attributeValue("page");
		Element newDiv = transformDivElement(div, "div");
		return tableEntry(newDiv.asXML(), page);
	}
	
	private String tableEntry(String text, String page) {
		if (page == null) {
			page = "";
		}
		return String.format(tableFmt, text, page);
	}

	private Element transformDivElement(Element div, String name) {

		String level = div.attributeValue("level");
		Element newDiv = DocumentHelper.createElement(name);
		newDiv.addAttribute("class", "level_" + level);
		newDiv.setText(div.getText());
		return newDiv;
	}

	private String pageFmt = "<h1>%s</h1>\n<table>\n<tr><td>\n"
			+ "<h2>%s</h2>\n</td><td>\n</td><td>\n<h2>(%s)</h2>\n"
			+ "</td></tr>\n</table>\n<hr />\n";
	
	private String tableFmt = "<tr><td>%s</td><td><div class=\"page\">%s</div></td></tr>";

	class IdComparator implements Comparator<Element> {

		@Override
		public int compare(Element elem0, Element elem1) {
			String id0 = elem0.attributeValue("id");
			String id1 = elem1.attributeValue("id");
			return id0.compareTo(id1);
		}

	}
}
