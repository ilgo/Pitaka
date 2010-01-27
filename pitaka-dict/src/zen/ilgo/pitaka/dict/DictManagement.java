package zen.ilgo.pitaka.dict;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zen.ilgo.pitaka.dict.IDict.IDictEntry;

public class DictManagement {

	private static DictManagement instance;

	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName;
	private String connectionURL; // = "jdbc:derby:" + dbName + ";create=true";
	private Connection conn;

	private static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private DictManagement() {

		try {
			initDerby();
		} catch (Exception e) {
			
			throw new RuntimeException("Error initializing Derby-DB: "
					+ e.getMessage());
		}
	}

	private void initDerby() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		dbName = System.getProperty("derby.dbname");

		System.out.println("derby-name:" + dbName);

		connectionURL = "jdbc:derby:" + dbName;
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(connectionURL);
		// System.out.println("Connected to database " + dbName);
	}

	public void shutdownDerby() {

		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
			boolean gotSQLExc = false;
			try {
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException se) {
				if (se.getSQLState().equals("XJ015")) {
					gotSQLExc = true;
				}
			}
			if (!gotSQLExc) {
				System.out.println("Database did not shut down normally");
			} else {
				System.out.println("Database shut down normally");
			}
		}
	}

	public static DictManagement getInstance() {
		if (instance == null) {
			instance = new DictManagement();
		}
		return instance;
	}

	public Connection getConnection() {
		return conn;
	}

	public void importDict(IDict dict) {

		try {
			int dictKey = importDictInfo(dict);
			importDictContent(dict, dictKey);
		} catch (SQLException e) {

			// TODO: remove the dict
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void removeDict(String name) {

		PreparedStatement stmt = PitakaSql.getRemoveDictStmt();
		try {
			stmt.setString(1, name);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param dict
	 * @return
	 * @throws SQLException
	 */
	private int importDictInfo(IDict dict) throws SQLException {

		PreparedStatement stmt = PitakaSql.getAddDictMetaStmt();
		stmt.setInt(1, dict.getEntryCount());
		stmt.setString(2, dict.getName());
		stmt.setString(3, dict.getAuthor());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		int key = 0;
		if (rs.next()) {
			key = rs.getInt(1);
		}
		rs.close();
		return key;
	}

	/**
	 * import content in batches of 100 entries each. definitions can be quite
	 * long, and therefore statements with 1000's of definitions might consume
	 * many MB of resources'
	 * 
	 * @param dict
	 * @param dictKey
	 * @throws NoSuchAlgorithmException
	 * @throws SQLException
	 */
	private void importDictContent(IDict dict, int dictKey)
			throws NoSuchAlgorithmException, SQLException {

		PreparedStatement defsStmt;
		PreparedStatement wordStmt;
		PreparedStatement countStmt = PitakaSql.getCountDefsStmt();

		MessageDigest md5 = MessageDigest.getInstance("MD5");

		int loopCount = dict.getEntryCount() / PitakaSql.DEFAULT_STMT_SIZE;
		
		ResultSet rs = countStmt.executeQuery();
		rs.next();
		int defCount = rs.getInt(1);
		defCount++;
		rs.close();

		for (int n = 0; n < loopCount; n++) {
			
			int defIndex = 1;
			int wordIndex = 1;
			
			defsStmt = PitakaSql.getAdd100DefsStmt();
			wordStmt = PitakaSql.getAdd100WordsStmt();
			
			for (int m = 0; m < PitakaSql.DEFAULT_STMT_SIZE; m++) {
				dict.hasNext();
				IDictEntry entry = dict.next();

				wordStmt.setInt(wordIndex++, defCount);
				wordStmt.setString(wordIndex++, entry.getWord());

				String def = entry.getDefinition();
				byte[] bytes = md5.digest(def.getBytes());
				String hash = byteToHex(bytes);
				md5.reset();

				defsStmt.setInt(defIndex++, defCount++);
				defsStmt.setInt(defIndex++, dictKey);
				defsStmt.setString(defIndex++, def);
				defsStmt.setString(defIndex++, hash);
			}
			defsStmt.executeUpdate();
			wordStmt.executeUpdate();
		}

		/*
		 * Add the last entries, must be less than 100.
		 */
		List<String> lastDefs = new ArrayList<String>(
				PitakaSql.DEFAULT_STMT_SIZE);
		List<String> lastWord = new ArrayList<String>(
				PitakaSql.DEFAULT_STMT_SIZE);
		List<String> lastHash = new ArrayList<String>(
				PitakaSql.DEFAULT_STMT_SIZE);
		
		while (dict.hasNext()) {
			IDictEntry entry = dict.next();			
			String def = entry.getDefinition();
			
			byte[] bytes = md5.digest(def.getBytes());
			String hash = byteToHex(bytes);
			md5.reset();

			lastWord.add(entry.getWord());
			lastDefs.add(def);
			lastHash.add(hash);
		}
		
		Statement stmt = conn.createStatement();
		
		String lastDefsStmt = PitakaSql.getLastDefsStmt(defCount, dictKey, lastDefs,
				lastHash);		
		String lastWordStmt = PitakaSql.getLastWordStmt(lastWord, defCount);
		
		stmt.executeUpdate(lastDefsStmt);		
		stmt.executeUpdate(lastWordStmt);
	}

	/**
	 * get md5 string from byte array
	 * 
	 * @param bytes
	 * @return
	 */
	private String byteToHex(byte[] bytes) {

		StringBuilder hex = new StringBuilder();
		for (byte byt : bytes) {
			int b = byt;
			if (b < 0) {
				b += 255;
			}
			int upper = b / 16;
			int lower = b % 16;
			hex.append(HEXDIGITS[upper]);
			hex.append(HEXDIGITS[lower]);
		}
		return hex.toString();
	}
}
