package zen.ilgo.pitaka.build;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	private void loadTables() throws IOException, SQLException {
		
		String dict_project = System.getProperty("dict.project.dir");
		File[] sqlFiles = new File(dict_project, "resources/createDictTables").listFiles();
		for (File sqlFile : sqlFiles) {
			byte[] bytes = new byte[(int)sqlFile.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sqlFile));
			bis.read(bytes);
			String sql = new String(bytes);
			s.execute(sql);
			System.out.println("Created Table: " + sqlFile.getName());
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

	public static void main(String[] args) {
		new BuildDictStuff();
		System.exit(0);
	}
}
