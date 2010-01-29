package zen.ilgo.pitaka.build;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildDictStuff {

	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName;
	private String connectionURL = "jdbc:derby:" + dbName + ";create=true";

	private Connection conn;
	private Statement s;

	public BuildDictStuff() {
		try {
			initDerby();
			loadTables();
			loadTriggers();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shutdownDerby();
		}
	}

	private void initDerby() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		dbName = System.getProperty("derby.dbname");
		connectionURL = "jdbc:derby:" + dbName + ";create=true";
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(connectionURL);
		s = conn.createStatement();
		System.out.println("Connected to database " + dbName);
	}

	private void loadTables() throws SQLException {

		String[] tableStatements = { createMetaTable, createDefsTable,
				createWordTable };
		executeCreationStatements(tableStatements);
	}
	
	private void loadTriggers() throws SQLException {

		String[] triggerStatements = { createAddDictTrigger, createRemoveDictTrigger };
		executeCreationStatements(triggerStatements);
	}
	
	private void executeCreationStatements(String[] statements) throws SQLException {
		for (String sql : statements) {
			s.execute(sql);
			System.out.println("Created Table: " + sql);
		}
	}

	private void shutdownDerby() {

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

	private final String createMetaTable = "CREATE TABLE meta ( "
			+ "	id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
			+ "	size INT NOT NULL,"
			+ "	name VARCHAR(256) NOT NULL,"
			+ "	author VARCHAR(256),"
			+ " used CHAR(1) NOT NULL CONSTRAINT usedConst CHECK (used IN ('Y', 'N')), "
			+ " useid SMALLINT NOT NULL )";

	private final String createDefsTable = "CREATE TABLE defs ( "
			+ "	id INT NOT NULL PRIMARY KEY,"
			+ "	dictid INT NOT NULL, "
			+ "	def CLOB(128 K) NOT NULL,"
			+ " md5 CHAR(32) NOT NULL, "
			+ " CONSTRAINT dict_fk FOREIGN KEY (dictid)"
			+ " REFERENCES meta ( id ) ON DELETE CASCADE ON UPDATE RESTRICT )";

	private final String createWordTable = "CREATE TABLE words ( "
			+ " defid INT NOT NULL, "
			+ " word VARCHAR(256) NOT NULL, "
			+ " PRIMARY KEY (word, defid), "
			+ " CONSTRAINT defs_fk FOREIGN KEY (defid)"
			+ " REFERENCES defs ( id ) ON DELETE CASCADE ON UPDATE RESTRICT )";

	private final String createAddDictTrigger = "CREATE TRIGGER addDictTrigger "
			+ " AFTER INSERT ON meta "
			+ " FOR EACH ROW "
			+ " UPDATE meta SET useid = MAX(useid) + 1 WHERE id = MAX(id) ";

	private final String createRemoveDictTrigger = "CREATE TRIGGER removeDictTrigger "
			+ " BEFORE DELETE ON meta "
			+ " FOR EACH ROW "
			+ " UPDATE meta SET useid = useid - 1 WHERE id = MAX(id) ";

	/**
	 * needed for the ant build script.
	 * 
	 * @param args
	 *            no-args are passed
	 */
	public static void main(String[] args) {
		new BuildDictStuff();
		System.exit(0);
	}
}
