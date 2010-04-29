package zen.ilgo.pitaka.core;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import zen.ilgo.pitaka.collections.CollectionsView;
import zen.ilgo.pitaka.dict.DefinitionView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "zen.ilgo.pitaka.core.perspective";
	
	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		layout.addView(CollectionsView.ID, IPageLayout.LEFT, 0.33f,
				editorArea);

		IFolderLayout bottom = layout.createFolder("bottom",
				IPageLayout.BOTTOM, 0.66f, editorArea);
		
		bottom.addView(DefinitionView.ID);
	}
	
	
}
