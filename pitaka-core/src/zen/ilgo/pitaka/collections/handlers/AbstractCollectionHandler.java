package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import zen.ilgo.pitaka.collections.CollectionsView;
import zen.ilgo.pitaka.db.DbCollection;

public class AbstractCollectionHandler extends AbstractHandler {

	final DbCollection dbCol;
	static TreeViewer treeViewer;

	public AbstractCollectionHandler() {
		super();
		dbCol = new DbCollection();
	}

	public static void setTreeViewer(TreeViewer treeViewer) {
		AbstractCollectionHandler.treeViewer = treeViewer;
	}

	protected void refresh(Object obj) {
		if (treeViewer != null) {
			treeViewer.refresh(obj);
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}
	
	protected ISelection getCollectionViewSelection(ExecutionEvent event) {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		CollectionsView view = (CollectionsView) page
				.findView(CollectionsView.ID);
		ISelection selection = view.getSite().getSelectionProvider()
				.getSelection();
		return selection;
	}

}
