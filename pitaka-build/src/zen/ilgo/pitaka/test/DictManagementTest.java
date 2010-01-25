package zen.ilgo.pitaka.test;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import zen.ilgo.pitaka.dict.DictManagement;
import zen.ilgo.pitaka.dict.IDict;
import zen.ilgo.pitaka.dict.star.Stardict;

public class DictManagementTest {

	static DictManagement instance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		System.setProperty("derby.dbname", "/home/ilgo/workspace/pitaka/derby/dicts");
		instance = DictManagement.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		instance.shutdownDerby();
	}

	@Test
	public void testAddDict() throws IOException {

		File ifo = new File("/home/ilgo/.stardict/dic/Bulkwang.ifo");
		IDict dict = new Stardict(ifo);
		instance.importDict(dict);
	}

	@Test
	public void testRemoveDict() {

		instance.removeDict("Bulkwang");
	}

}
