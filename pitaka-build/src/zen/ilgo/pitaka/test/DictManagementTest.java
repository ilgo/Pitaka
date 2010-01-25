package zen.ilgo.pitaka.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import zen.ilgo.pitaka.dict.DictManagement;
import zen.ilgo.pitaka.dict.IDict;
import zen.ilgo.pitaka.dict.star.Stardict;

public class DictManagementTest {

	static final String dictName = "Bulkwang";
	
	static DictManagement instance;
	static Statement stmt;
	static String wordsCheck = "SELECT COUNT(*) FROM words";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		System.setProperty("derby.dbname",
				"/home/ilgo/workspace/pitaka/derby/dicts");
		instance = DictManagement.getInstance();
		stmt = instance.getConnection().createStatement();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		instance.shutdownDerby();
	}

	@Test
	public void testAddDict() throws IOException, SQLException {

		File ifo = new File("/home/ilgo/.stardict/dic/" + dictName + ".ifo");
		IDict dict = new Stardict(ifo);
		instance.importDict(dict);
		ResultSet rs = stmt.executeQuery(wordsCheck);
		int wordCount = getResultAsInt(rs);
		rs.close();
		assertEquals(wordCount, dict.getEntryCount());
	}

	@Test
	public void testRemoveDict() throws SQLException {

		instance.removeDict(dictName);
		ResultSet rs = stmt.executeQuery(wordsCheck);
		int wordCount = getResultAsInt(rs);
		rs.close();
		assertEquals(wordCount, 0);
	}

	private static int getResultAsInt(ResultSet rs) throws SQLException {
		rs.next();
		int count = rs.getInt(1);
		return count;
	}

}
