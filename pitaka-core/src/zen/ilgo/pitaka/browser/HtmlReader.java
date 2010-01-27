package zen.ilgo.pitaka.browser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import zen.ilgo.pitaka.dict.PitakaSql;

public class HtmlReader extends EditorPart {

	public static String ID = "zen.ilgo.pitaka.HtmlReader";
	private static final String BROWSER_SELECT = "document.onmouseup = function() {if (window.getSelection) { window.status = 'BROWSER_SELECT'+window.getSelection();	 }	else if (document.getSelection) { window.status = 'BROWSER_SELECT'+document.getSelection(); } else if (document.selection) {	window.status = 'BROWSER_SELECT'+document.selection.createRange().text; };	window.status='';}";

	private Browser browser;
	private String url;

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

		setPartName(((HtmlEditorInput) input).getName());
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
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				browser.execute(BROWSER_SELECT);
			}
		});

		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				if (event.text.startsWith("BROWSER_SELECT")) {
					String selection = event.text.substring("BROWSER_SELECT"
							.length());
					if (selection != null && !"".equals(selection)) {

						PreparedStatement wordDefStmt = PitakaSql
								.getWordDefStmt();
						try {
							
							System.out.println(selection);
							wordDefStmt.setString(1, selection);
							ResultSet rs = wordDefStmt.executeQuery();
							rs.next();
							
							String def = rs.getString(1);
							rs.close();
							System.out.println(def);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("Not found");
						}
					}
				}
			}
		});
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
