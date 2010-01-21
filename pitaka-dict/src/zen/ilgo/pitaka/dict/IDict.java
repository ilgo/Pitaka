package zen.ilgo.pitaka.dict;

import java.util.Iterator;

/**
 * 
 * @author ilgo
 * 
 */
public interface IDict extends Iterator<IDict.IDictEntry> {

	public interface IDictEntry {

		String getWord();

		String getDefinition();
	}

	public String getName();

	public String getAuthor();

	public int getEntryCount();
	
	public void dispose();
}
