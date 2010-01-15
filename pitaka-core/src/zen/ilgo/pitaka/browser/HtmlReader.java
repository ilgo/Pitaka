package zen.ilgo.pitaka.browser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class HtmlReader extends EditorPart {

	public static String ID = "zen.ilgo.pitaka.core.HtmlReader";
	Browser browser;
	String url;

	public HtmlReader() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		url = ((HtmlEditorInput) input).getUrl();
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void createPartControl(Composite parent) {
		
		browser = new Browser(parent, SWT.MOZILLA);
		browser.setUrl(url);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	public void navigateTo(String url) {
		browser.setUrl(url);
		browser.refresh();
	}

}
