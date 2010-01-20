package zen.ilgo.pitaka.dict;

import java.util.Iterator;

/**
 * This interface shall allow me to load a dict of
 * any kind and get either all entries as iterable
 * items or by searching for a single named entry.
 * 
 * @author ilgo
 * @since 21/12/08
 */
public interface IDictData extends Iterator<String> {

	String getDefinition(String entry);
	
	
}
