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
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

		String[] createStatements = { createMetaTable, createDefsTable,
				createWordTable };
		for (String sql : createStatements) {
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
			+ "	author VARCHAR(256) )";

	private final String createDefsTable = "CREATE TABLE defs ( "
			+ "	id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
			+ "	dictid INT NOT NULL, " 
			+ "	def CLOB(128 K) NOT NULL,"
			+ " md5 CHAR(32) NOT NULL, "
			+ " CONSTRAINT dict_fk FOREIGN KEY (dictid)"
			+ " REFERENCES meta ( id ) ON DELETE CASCADE ON UPDATE RESTRICT )";

	private final String createWordTable = "CREATE TABLE words ( "
			+ " defid INT NOT NULL, "
			+ " word VARCHAR(512) NOT NULL, "
			+ " PRIMARY KEY (defid, word), "
			+ " CONSTRAINT defs_fk FOREIGN KEY (defid)"
			+ " REFERENCES defs ( id ) ON DELETE CASCADE ON UPDATE RESTRICT )";			

	/**
	 * needed for the ant build script.
	 * @param args no-args are passed
	 */
	public static void main(String[] args) {
		new BuildDictStuff();
		System.exit(0);
	}
}
