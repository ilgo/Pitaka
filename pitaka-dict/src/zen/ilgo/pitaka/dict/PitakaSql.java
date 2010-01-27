package zen.ilgo.pitaka.dict;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PitakaSql {

	private static Connection conn;
	static {
		conn = DictManagement.getInstance().getConnection();
	}

	public final static int DEFAULT_STMT_SIZE = 100;

	private static PreparedStatement addDictMetaStmt = null;
	private static PreparedStatement add100DefsStmt = null;
	private static PreparedStatement add100WordsStmt = null;
	private static PreparedStatement removeDictStmt = null;
	private static PreparedStatement countDefsStmt = null;
	private static PreparedStatement wordDefStmt = null;

	public static PreparedStatement getAddDictMetaStmt() {
		if (addDictMetaStmt == null) {
			generateAddDictMetaStmt();
		}
		return addDictMetaStmt;
	}

	public static PreparedStatement getAdd100DefsStmt() {
		if (add100DefsStmt == null) {
			generateAdd100DefsStmt();
		}
		return add100DefsStmt;
	}

	public static PreparedStatement getAdd100WordsStmt() {
		if (add100WordsStmt == null) {
			generateAdd100WordsStmt();
		}
		return add100WordsStmt;
	}

	public static PreparedStatement getRemoveDictStmt() {
		if (removeDictStmt == null) {
			generateRemoveDictStmt();
		}
		return removeDictStmt;
	}

	public static PreparedStatement getCountDefsStmt() {
		if (countDefsStmt == null) {
			generateCountDefsStmt();
		}
		return countDefsStmt;
	}

	public static PreparedStatement getWordDefStmt() {
		if (wordDefStmt == null) {
			generateWordDefStmt();
		}
		return wordDefStmt;
	}

	public static String getAddDefsSql(int n) {
		return generateAddDefsSql(n);
	}

	public static String getAddWordsSql(int n) {
		return generateAddWordsSql(n);
	}

	public static String getLastDefsStmt(int defId, int dictKey,
			List<String> lastDefs, List<String> lastHash) {

		int id = defId;
		String fmt = "(%d, %d, '%s', '%s')";
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO defs (id, dictid, def, md5) VALUES \n");
		for (int n = 0; n < lastDefs.size() - 1; n++) {
			sb.append(String.format(fmt, id++, dictKey, lastDefs.get(n),
					lastHash.get(n)));
			sb.append(",\n");
		}
		int n = lastDefs.size() - 1;
		sb.append(String.format(fmt, id, dictKey, lastDefs.get(n), lastHash
				.get(n)));
		return sb.toString();
	}

	public static String getLastWordStmt(List<String> lastWord, int defId)
			throws SQLException {

		int id = defId;
		String fmt = "(%d, '%s')";
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO words (defid, word) VALUES \n");
		for (int n = 0; n < lastWord.size() - 1; n++) {
			sb.append(String.format(fmt, id++, lastWord.get(n)));
			sb.append(",\n");
		}
		int n = lastWord.size() - 1;
		sb.append(String.format(fmt, id, lastWord.get(n)));
		return sb.toString();
	}

	private static void generateAddDictMetaStmt() {

		String sql = "INSERT INTO meta (size, name, author) VALUES (?, ?, ?)";
		try {
			addDictMetaStmt = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateAdd100DefsStmt() {

		String sql = generateAddDefsSql(DEFAULT_STMT_SIZE);
		try {
			add100DefsStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateAdd100WordsStmt() {

		String sql = generateAddWordsSql(DEFAULT_STMT_SIZE);
		try {
			add100WordsStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateRemoveDictStmt() {

		String sql = "DELETE FROM meta WHERE name = ?";
		try {
			removeDictStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateCountDefsStmt() {

		String sql = "SELECT MAX(id) FROM defs";
		try {
			countDefsStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void generateWordDefStmt() {
		
		String sql = "SELECT def FROM defs JOIN words ON defid = id WHERE word = ?"; 
		try {
			wordDefStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String generateAddDefsSql(int n) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO defs (id, dictid, def, md5) VALUES \n");
		for (int row = 0; row < n - 1; row++) {
			sb.append("(?, ?, ?, ?),\n");
		}
		sb.append("(?, ?, ?, ?)");
		return sb.toString();
	}

	private static String generateAddWordsSql(int n) {

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO words (defid, word) VALUES \n");
		for (int row = 0; row < n - 1; row++) {
			sb.append("(?, ?),\n");
		}
		sb.append("(?, ?)");
		return sb.toString();
	}
}
