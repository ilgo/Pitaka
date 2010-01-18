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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

public class CreateCollectionFromDir extends AbstractCollectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = getCollectionViewSelection(event);
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final Object object = strucSelection.getFirstElement();
			if (object instanceof Collection) {

				Shell shell = HandlerUtil.getActiveWorkbenchWindow(event)
						.getShell();
				DirectoryDialog dirDialog = new DirectoryDialog(shell);
				dirDialog.setText("Select Resource Directory");
				String selected = dirDialog.open();
				final File dir = new File(selected);

				if (dir.getName() != null) {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							HandlerUtil.getActiveShell(event).getShell());
					try {
						IRunnableWithProgress storeTask = new StoreDirResourcesTask(
								(Collection) object, dir);
						dialog.run(true, true, storeTask);
						refresh(object);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private class StoreDirResourcesTask implements IRunnableWithProgress {

		private final Collection root;
		private final File dir;
		private Collection col;

		private File[] resources;
		private int[] sizes;
		private long totalSize;

		public StoreDirResourcesTask(Collection root, File dir) {
			this.root = root;
			this.dir = dir;
		}

		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {

			preparations(monitor);
			getResourceSizes();
			storeResources(monitor);
			monitor.done();
		}

		private void preparations(IProgressMonitor monitor) {

			monitor
					.beginTask("Preparing to import Resources.",
							(int) totalSize);
			resources = dir.listFiles();

			try {
				System.out.println("new Col: " + root.getName() + "/"
						+ dir.getName());
				col = dbCol.createCollection(root.getName() + "/"
						+ dir.getName());
			} catch (XMLDBException e) {
				e.printStackTrace();
			}
		}

		private void getResourceSizes() {

			int len = resources.length;
			sizes = new int[len];
			for (int n = 0; n < len; n++) {
				long fileLen = resources[n].length();
				sizes[n] = (int) fileLen;
				totalSize += fileLen;
			}
		}

		private void storeResources(IProgressMonitor monitor) {

			for (int n = 0; n < resources.length; n++) {
				File resource = resources[n];
				monitor.subTask("Storing Resource " + resource.getName());
				dbCol.storeResource(col, resource);
				// worked += sizes[n];
				monitor.worked(sizes[n]);
			}
		}
	}
}
