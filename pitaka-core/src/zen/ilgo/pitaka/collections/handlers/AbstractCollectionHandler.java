package zen.ilgo.pitaka.collections.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;

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

}
