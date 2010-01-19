package zen.ilgo.pitaka.collections;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.xmldb.api.base.Collection;

import zen.ilgo.pitaka.collections.handlers.AbstractCollectionHandler;
import zen.ilgo.pitaka.db.Session;

public class CollectionsView extends ViewPart {

	TreeViewer treeViewer;

	public static final String ID = "zen.ilgo.pitaka.core.collections";

	public CollectionsView() {
		super();
	}

	public void createPartControl(Composite parent) {

		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		treeViewer.setLabelProvider(new ExistTreeLabelProvider());
		treeViewer.setContentProvider(new ExistTreeContentProvider());
		treeViewer.addDoubleClickListener(new TreeClicker());
		AbstractCollectionHandler.setTreeViewer(treeViewer);
		getSite().setSelectionProvider(treeViewer);
		Collection root = Session.getInstance().getRootCollection();
		treeViewer.setInput(root);

		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the MenuManager
		treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, treeViewer);
		// Make the selection available
		getSite().setSelectionProvider(treeViewer);

	}

	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	class TreeClicker implements IDoubleClickListener {

		@Override
		public void doubleClick(DoubleClickEvent event) {

			String obj = event.getSelection().toString();
			if (obj.contains("Resource")) {

				IHandlerService handlerService = (IHandlerService) getSite()
						.getService(IHandlerService.class);
				try {
					handlerService.executeCommand("zen.ilgo.pitaka.collections.handlers.OpenReaders",
							null);

				} catch (Exception ex) {					
					ex.printStackTrace();
				}
			}

		}

	}

}
