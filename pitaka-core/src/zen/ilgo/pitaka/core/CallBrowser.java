package zen.ilgo.pitaka.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import zen.ilgo.pitaka.browser.HtmlEditorInput;
import zen.ilgo.pitaka.browser.HtmlReader;
import zen.ilgo.pitaka.collections.CollectionsView;
import zen.ilgo.pitaka.db.Session;
import zen.ilgo.pitaka.server.StartServer;

public class CallBrowser extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		CollectionsView view = (CollectionsView) page
				.findView(CollectionsView.ID);
		ISelection selection = view.getSite().getSelectionProvider()
				.getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();

			if (obj != null && obj instanceof Resource) {
				try {
					//String contentPage = queryContents((Resource) obj);
					Resource resource = (Resource) obj;
					String id = resource.getId();
					String path = resource.getParentCollection().getName();
					
					String url = StartServer.SERVER + "/content?id=" + path + "/" + id;
					
					System.out.println(url);
					HtmlEditorInput input = new HtmlEditorInput(url);
					page.openEditor(input, HtmlReader.ID);

				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private String queryContents(Resource resource) throws XMLDBException {

		Collection col = resource.getParentCollection();
		XPathQueryService service = Session.getInstance().getQueryService(col);

		InputStream is = Session.getInstance().getResource("resources/queries/yinshun.content.xq");
		BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		int read;
		try {
			while((read = bis.read(buffer)) != -1) {
				sb.append(buffer, 0, read);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = sb.toString();
		query = query.replace("DOCUMENT", resource.getId());
		sb = new StringBuilder();
		ResourceSet resultSet = service.query(query);
		ResourceIterator i = resultSet.getIterator();
		while (i.hasMoreResources()) {
			Resource r = i.nextResource();
			sb.append((String)r.getContent());
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}
}
