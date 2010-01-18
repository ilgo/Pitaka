package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

public class DeleteCollectionHandler extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object object = strucSelection.getFirstElement();
			//dbCol.removeCollection(object);

			if (object instanceof Collection) {
				try {

					Collection col = (Collection) object;
					String root = col.getName();
					if (root.equals("/db/Texts") || root.equals("/db/Users")) {
						// do not allow to delete the basic collections
					} else {
						Collection parent  = dbCol.removeCollection(col);
						refresh(parent);
					}
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}