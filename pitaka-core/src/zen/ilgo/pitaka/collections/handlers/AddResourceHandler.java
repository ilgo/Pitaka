package zen.ilgo.pitaka.collections.handlers;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;

public class AddResourceHandler extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object object = strucSelection.getFirstElement();
			if (object instanceof Collection) {

				Shell shell = HandlerUtil.getActiveWorkbenchWindow(event)
						.getShell();
				FileDialog fileDialog = new FileDialog(shell);
				fileDialog.setText("Select File");
				fileDialog.setFilterExtensions(new String[] { "*.xml" });
				fileDialog.setFilterNames(new String[] { "XML files(*.xml)" });
				String selected = fileDialog.open();
				File file = new File(selected);

				dbCol.storeResource((Collection) object, file);
				refresh(object);
			}
		}
		return null;
	}

}
