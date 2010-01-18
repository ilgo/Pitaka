package zen.ilgo.pitaka.collections.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;

public class AddResourceHandler extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = getCollectionViewSelection(event);
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final Object object = strucSelection.getFirstElement();
			if (object instanceof Collection) {

				final Collection col = (Collection) object;
				Shell shell = HandlerUtil.getActiveWorkbenchWindow(event)
						.getShell();
				FileDialog fileDialog = new FileDialog(shell);
				fileDialog.setText("Select File");
				fileDialog.setFilterExtensions(new String[] { "*.xml" });
				fileDialog.setFilterNames(new String[] { "XML files(*.xml)" });
				String selected = fileDialog.open();
				final File file = new File(selected);

				ProgressMonitorDialog dialog = new ProgressMonitorDialog(
						HandlerUtil.getActiveShell(event).getShell());
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) {
							monitor.beginTask("Storing Resource:"
									+ file.getName(), 100);
							dbCol.storeResource(col, file);
							monitor.worked(100);
							monitor.done();
						}
					});
					refresh(object);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
