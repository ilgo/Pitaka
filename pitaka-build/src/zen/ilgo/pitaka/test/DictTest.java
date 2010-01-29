package zen.ilgo.pitaka.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import zen.ilgo.pitaka.dict.IDict;
import zen.ilgo.pitaka.dict.IDict.IDictEntry;
import zen.ilgo.pitaka.dict.star.Stardict;

public class DictTest {

	static IDict instance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		File ifo_1 = new File("resources/testDicts/dict_1.ifo");
		File ifo_2 = new File("resources/testDicts/dict_2.ifo");
		instance = new Stardict(ifo_1);
	}

	@Test
	public void testGetAuthor() {
		String author = instance.getAuthor();
		assertTrue("ilgo".equals(author));
	}

	@Test
	public void testGetEntryCount() {
		int entryCount = instance.getEntryCount();
		assertTrue(entryCount == 5);
	}

	@Test
	public void testGetName() {
		String name = instance.getName();
		assertTrue("dict_1".equals(name));
	}

	@Test
	public void testNext() {

		int counter = 0;
		while (instance.hasNext()) {
			IDictEntry entry = instance.next();
			System.out.println(entry.getWord() + " -- " + entry.getDefinition());
			counter++;
		}
		instance.dispose();
		assertTrue(counter == instance.getEntryCount());
	}
}
