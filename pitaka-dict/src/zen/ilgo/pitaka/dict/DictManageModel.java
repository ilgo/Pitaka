package zen.ilgo.pitaka.dict;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DictManageModel {

	private List<String> unusedDicts;
	private List<String> usingDicts;

	public DictManageModel() throws SQLException {
		unusedDicts = new ArrayList<String>();
		usingDicts = new ArrayList<String>();
		initModel();
	}

	public void moveToTop(String dict) {

		int idx = usingDicts.indexOf(dict);
		if (idx > 0) {
			usingDicts.remove(idx);
			usingDicts.add(0, dict);
		}
	}

	public void moveToBottom(String dict) {

		int idx = usingDicts.indexOf(dict);
		int last = usingDicts.size() - 1;
		if (idx < last) {
			usingDicts.remove(idx);
			usingDicts.add(dict);
		}
	}

	public void moveUp(String dict) {

		int idx = usingDicts.indexOf(dict);
		if (idx > 0) {
			usingDicts.remove(idx);
			usingDicts.add(--idx, dict);
		}
	}

	public void moveDown(String dict) {

		int idx = usingDicts.indexOf(dict);
		int last = usingDicts.size() - 1;
		if (idx < last) {
			usingDicts.remove(idx);
			usingDicts.add(++idx, dict);
		}
	}

	public void moveToUsing(String dict) {

		unusedDicts.remove(dict);
		usingDicts.add(dict);
	}

	public void moveToUnused(String dict) {

		usingDicts.remove(dict);
		unusedDicts.add(dict);
	}

	public void pesistSettings() {

	}

	public void ignoreSettings() {
		try {
			initModel();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getUsingDicts() {
		return usingDicts.toArray(new String[usingDicts.size()]);
	}

	public String[] getUnusedDicts() {
		return unusedDicts.toArray(new String[unusedDicts.size()]);
	}

	private void initModel() throws SQLException {

		usingDicts.clear();
		unusedDicts.clear();
		PreparedStatement stmt = PitakaSql.getDictSearchPrefStmt();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1);
			boolean isUsed = rs.getString(2).equals("Y") ? true : false;
			int useId = rs.getInt(3);
			
			System.out.println(name + "-" + isUsed + "-" + useId);
			
			if (isUsed) {
				usingDicts.add(useId - 1, name);
			} else {
				unusedDicts.add(name);
			}
		}
	}
}
