package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

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

					Collection col = (Collection) object;
					Shell shell = HandlerUtil.getActiveWorkbenchWindow(event)
							.getShell();
					CreateCollectionDialog dialog = new CreateCollectionDialog(
							shell, col.getName());
					if (dialog.open() == Window.OK) {
						String colName = dialog.getValue();
						dbCol.createCollection(col.getName() + "/" + colName);
						refresh(object);
					}

				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Wraps an InputDialog So i dont need to pass all thos params to the
	 * constructor
	 * 
	 * @author ilgo
	 * 
	 */
	class CreateCollectionDialog {

		private final InputDialog inputDialog;

		public CreateCollectionDialog(Shell parentShell, String parentName) {
			String dialogTitle = "Create New Collection";
			String dialogMessage = "Enter a Collection Name";
			inputDialog = new InputDialog(parentShell, dialogTitle,
					dialogMessage, "", null);
		}

		public String getValue() {
			return inputDialog.getValue();
		}

		public int open() {
			return inputDialog.open();
		}
	}
}
