package zen.ilgo.pitaka.dict.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import zen.ilgo.pitaka.dict.DictManagement;
import zen.ilgo.pitaka.dict.PitakaSql;

public class DeleteDictHandler implements IHandler {

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
		
		try {
			String[] allDictNames = getAllDictNames();
			Shell shell = PlatformUI.getWorkbench().getDisplay()
					.getActiveShell();
			ListSelectionDialog dlg = new ListSelectionDialog(shell,
					allDictNames, new ArrayContentProvider(),
					new LabelProvider(), "Select the Dictionaries to delete");
			dlg.setTitle("Delete Dictionaries");
			dlg.open();
			DictManagement manage = DictManagement.getInstance();
			for (Object dict : dlg.getResult()) {
				manage.removeDict((String) dict);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private String[] getAllDictNames() throws SQLException {

		List<String> dictNames = new ArrayList<String>();
		PreparedStatement stmt = PitakaSql.getAllDictNamesStmt();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			dictNames.add(rs.getString(1));
		}
		return dictNames.toArray(new String[dictNames.size()]);
	}

}
