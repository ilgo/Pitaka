package zen.ilgo.pitaka.core;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import zen.ilgo.pitaka.collections.CollectionsView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		layout.addStandaloneView(CollectionsView.ID, false, IPageLayout.LEFT,
				0.3f, editorArea);
	}
}
