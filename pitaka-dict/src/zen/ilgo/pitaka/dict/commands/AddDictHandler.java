package zen.ilgo.pitaka.dict.commands;

import java.io.File;
import java.io.IOException;

import org.dict.zip.DictzipException;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import zen.ilgo.pitaka.dict.DictManagement;
import zen.ilgo.pitaka.dict.IDict;
import zen.ilgo.pitaka.dict.star.Stardict;

public class AddDictHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		FileDialog fileDlg = new FileDialog(shell);
		String home = System.getProperty("user.home");
		fileDlg.setFilterPath(home + "/.stardict/dic/");
		fileDlg.setFilterExtensions(new String[] { "*.ifo" });
		String ifo = fileDlg.open();
		if (ifo != null) {
			try {
				File ifoFile = new File(ifo);
				IDict stardict = new Stardict(ifoFile);
				int status = DictManagement.getInstance().importDict(stardict);
				if (status == -1) {
					String message = "A Dictionary named " + stardict.getName()
							+ " already exists in Database.";
					MessageDialog.openWarning(shell, "Dict Import", message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DictzipException e) {
				MessageDialog.openError(shell, "Dict Import", e.getMessage());
			}
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
