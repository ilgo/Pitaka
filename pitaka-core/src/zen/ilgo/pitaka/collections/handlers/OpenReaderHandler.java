package zen.ilgo.pitaka.collections.handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import zen.ilgo.pitaka.browser.HtmlEditorInput;
import zen.ilgo.pitaka.browser.HtmlReader;
import zen.ilgo.pitaka.collections.CollectionsView;
import zen.ilgo.pitaka.server.JettyServer;

public class OpenReaderHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		CollectionsView view = (CollectionsView) page
				.findView(CollectionsView.ID);
		ISelectionProvider selectionProvider = view.getSite()
				.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();

			if (obj != null && obj instanceof Resource) {
				try {
					// String contentPage = queryContents((Resource) obj);
					Resource resource = (Resource) obj;
					String id = resource.getId();
					String path = resource.getParentCollection().getName();
					String url = JettyServer.SERVER + "/content?id=" + path
							+ "/" + id;

					String decodedID = URLDecoder.decode(id, "UTF-8");
					HtmlEditorInput input = new HtmlEditorInput(url, decodedID);
					page.openEditor(input, HtmlReader.ID);
					selection = selectionProvider.getSelection();
					obj = ((IStructuredSelection) selection).getFirstElement();

				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
