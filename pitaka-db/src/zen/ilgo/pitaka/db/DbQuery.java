package zen.ilgo.pitaka.db;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class DbQuery {

	public String getPage(Resource resource, int n) {

		StringBuilder result = new StringBuilder();
		try {
			Collection col = resource.getParentCollection();
			XPathQueryService service = Session.getInstance().getQueryService(
					col);

			String pageBegin = Integer.toString(n);
			String pageEnd = Integer.toString(n + 1);
			String queryStr = String.format(PAGE_XQ, resource.getId(),
					pageBegin, pageEnd);

			System.out.println(queryStr);

			ResourceSet resultSet = service.query(queryStr);
			ResourceIterator i = resultSet.getIterator();
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				result.append((String) r.getContent());
				result.append("\n");
			}
			System.out.println(result);

		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();
	}

	private final String PAGE_XQ = "xquery version \"1.0\";\n"
			+ "let $document := doc(\"%s\")\n"
			+ "let $title := $document//title/text()\n"
			+ "let $body := $document//body/*\n"
			+ "let $pages := $document//pb[@type=\"html\"]\n"
			+ "let $idx1 := index-of($body, $pages[position() = 1])\n"
			+ "let $idxs := insert-before($idx1, count($idx1)+1, count($body))\n"
			+ "return ($title, count($body), $idxs ,$body[position() = ($idxs[%s] to $idxs[%s])])";
}
