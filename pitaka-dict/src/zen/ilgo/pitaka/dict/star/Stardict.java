package zen.ilgo.pitaka.dict.star;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.dict.zip.DictZipDataAccessor;
import org.dict.zip.IDataAccessor;

import zen.ilgo.pitaka.dict.IDict;

public class Stardict implements IDict {

	private final Properties ifo;
	private final DataInputStream index;
	private final IDataAccessor dict;
	private final IDictEntry entry;
	private final String bookName;

	/**
	 * the constructor. will load the dict and idx files, by getting the base
	 * info from the associated ifo file
	 * 
	 * @param ifoFile
	 *            the stardict's ifo file
	 * @throws IOException
	 */
	public Stardict(File ifoFile) throws IOException {

		ifo = loadIfoProperties(ifoFile);

		File baseDir = ifoFile.getParentFile();
		bookName = ifo.getProperty("bookname");

		index = loadIndexFile(baseDir);
		dict = loadDictFile(baseDir);

		entry = new StarEntry();
	}

	@Override
	public String getAuthor() {
		return ifo.getProperty("author");
	}

	@Override
	public int getEntryCount() {
		return Integer.valueOf(ifo.getProperty("wordcount"));
	}

	@Override
	public String getName() {
		return bookName;
	}

	@Override
	public boolean hasNext() {

		try {
			((StarEntry) entry).setWord(nextWord());
			((StarEntry) entry).setDefinition(nextDefinition());
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	@Override
	public IDictEntry next() {

		return entry;
	}

	@Override
	public void remove() {

		throw new UnsupportedOperationException("Removal is not supported");
	}

	@Override
	public void dispose() {

		dict.dispose();
		if (index != null) {
			try {
				index.close();
			} catch (IOException e) {

			}
		}
	}

	private String nextWord() throws IOException {

		byte[] wordBuffer = new byte[256];
		int i = 0;
		byte b = index.readByte();
		while (b != '\0') {
			wordBuffer[i++] = b;
			b = index.readByte();
		}
		//wordBuffer[i++] = b;
		return new String(wordBuffer).trim();
	}

	private String nextDefinition() {

		String definition = null;
		try {
			int offset = index.readInt();
			int len = index.readInt();
			byte[] buffer = dict.readData(offset, len);
			definition = new String(buffer);
		} catch (IOException ioe) {
			definition = "";
		}
		return definition;
	}

	private Properties loadIfoProperties(File ifoFile) {

		Properties ifoProp = new Properties();
		InputStreamReader fis = null;
		try {
			fis = new InputStreamReader(new FileInputStream(ifoFile), "UTF-8");
			ifoProp.load(fis);
		} catch (IOException ioe) {

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ioe) {

				}
			}
		}
		return ifoProp;
	}

	private DataInputStream loadIndexFile(File baseDir) throws IOException {

		String[] types = { ".idx", ".idx.gz" };
		File indexFile = findExistingFile(baseDir, types);
		if (indexFile == null) {
			throw new FileNotFoundException("No index for " + bookName
					+ "found.");
		}

		InputStream fis = new FileInputStream(indexFile);
		if (indexFile.getName().endsWith(".gz")) {
			fis = new GZIPInputStream(fis);
		}
		return new DataInputStream(new BufferedInputStream(fis));

	}

	private IDataAccessor loadDictFile(File baseDir) throws IOException {

		String[] types = { ".dict", ".dict.dz" };
		File dictFile = findExistingFile(baseDir, types);
		if (dictFile == null) {
			throw new FileNotFoundException("No dict for " + bookName
					+ "found.");
		}
		return new DictZipDataAccessor(dictFile);
	}

	private File findExistingFile(File baseDir, String[] types) {

		for (String type : types) {
			File file = new File(baseDir, bookName + type);
			if (file.exists()) {
				//System.out.println("Found: " + file.getAbsolutePath());
				return file;
			}
		}
		return null;
	}

	private class StarEntry implements IDictEntry {

		private String word;
		private String definition;

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public String getDefinition() {
			return definition;
		}

		public void setDefinition(String definition) {
			this.definition = definition;
		}
	}

	public static void main(String[] args) throws IOException {
		File ifo = new File("/home/ilgo/.stardict/dic/Bulkwang.ifo");
		IDict instance = new Stardict(ifo);
		IDictEntry entry;
		int counter = 0;
		while (instance.hasNext()) {
			entry = instance.next();

			String word = entry.getWord();
			if (word.equals("")) {
				System.out.println(counter);
				break;
			}

			counter++;
		}
		instance.dispose();
	}
}
