package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import zen.ilgo.pitaka.dialogs.CreateCollectionDialog;

public class CreateCollectionHandler extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object object = strucSelection.getFirstElement();
			if (object instanceof Collection) {
				try {

					CreateCollectionDialog dialog = new CreateCollectionDialog(
							HandlerUtil.getActiveWorkbenchWindow(event)
									.getShell());
					dialog.create();
					if (dialog.open() == Window.OK) {
						String colName = dialog.getCollectionName();
						if (colName != null) {
							String root = ((Collection) object).getName();
							dbCol.createCollection(root + "/" + colName);
							refresh(object);
						}
					}

					String root = ((Collection) object).getName();
					dbCol.createCollection(root + "/"
							+ dialog.getCollectionName());
					refresh(object);
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
