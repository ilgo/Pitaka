package zen.ilgo.pitaka.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class GetPageFunction extends BrowserFunction {

	public GetPageFunction(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
	}
	
	public Object function (Object[] args) {
		
		
		StringBuilder result = new StringBuilder();
//		try {
//			Collection col = resource.getParentCollection();
//			XPathQueryService service = Session.getInstance().getQueryService(col);
//			
//			InputStream is = Session.getInstance().getResource("resources/queries/yinshun.page.xq");
//			BufferedReader bis = new BufferedReader(new InputStreamReader(is));
//			StringBuilder sb = new StringBuilder();
//			char[] buffer = new char[1024];
//			int read;
//			try {
//				while((read = bis.read(buffer)) != -1) {
//					sb.append(buffer, 0, read);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String queryFmt = sb.toString();
//
//			String pageBegin = Integer.toString(n);
//			String pageEnd = Integer.toString(n+1);
//			String queryStr = String.format(queryFmt, resource.getId(), pageBegin, pageEnd);
//			
//			System.out.println(queryStr);
//			
//			ResourceSet resultSet = service.query(queryStr);
//			ResourceIterator i = resultSet.getIterator();
//			while (i.hasMoreResources()) {
//				Resource r = i.nextResource();
//				result.append((String)r.getContent());
//				result.append("\n");
//			}
//			System.out.println(result);
//			
//		} catch (XMLDBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return result.toString();
	}

}
