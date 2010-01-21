package zen.ilgo.pitaka.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import zen.ilgo.pitaka.dict.IDict;
import zen.ilgo.pitaka.dict.star.Stardict;

public class DictTest {

	static IDict instance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		File ifo = new File("/home/ilgo/.stardict/dic/Bulkwang.ifo");
		instance = new Stardict(ifo);
	}

	@Test
	public void testGetAuthor() {
		String author = instance.getAuthor();
		assertTrue("ilgo Sunim (釋日高)".equals(author));
	}

	@Test
	public void testGetEntryCount() {
		int entryCount = instance.getEntryCount();
		assertTrue(entryCount == 22646);
	}

	@Test
	public void testGetName() {
		String name = instance.getName();
		assertTrue("Bulkwang".equals(name));
	}

	@Test
	public void testNext() {

		int counter = 0;
		while (instance.hasNext()) {
			instance.next();
			counter++;
		}
		instance.dispose();
		assertTrue(counter == instance.getEntryCount());
	}

}
