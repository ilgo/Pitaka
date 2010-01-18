package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;

public class DeleteResourceHandler extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = getCollectionViewSelection(event);
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object object = strucSelection.getFirstElement();
			if (object instanceof Resource) {
				Resource resource = (Resource) object;
				Collection parent = dbCol.removeResource(resource);
				refresh(parent);
			}
		}
		return null;
	}
}
