package zen.ilgo.pitaka.dict;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Properties;

public class StardictData implements IDictData {
	
	private final Properties ifo;
	private final DataInputStream index;
	private final RandomAccessFile dict;
	private IndexData idxData;
	
	/**
	 * the constructor.
	 * will load the dict and idx files, by getting
	 * the base info from the associated ifo file
	 * 
	 * @param ifoFile the stardict's ifo file
	 * @throws FileNotFoundException 
	 */
	public StardictData(File ifoFile) throws FileNotFoundException {
		
		ifo = loadIfoProperties(ifoFile);
		
		File baseDir = null;
		String baseName = ifo.getProperty("bookname");
		
		File idxFile = new File(baseDir, baseName + ".idx");
		index = new DataInputStream(new FileInputStream(idxFile));
		
		File dicFile = new File(baseDir, baseName + ".dict");
		dict = new RandomAccessFile(dicFile, "r");
		
		idxData = new IndexData();
	}

	@Override
	public boolean hasNext() {
		
		if (idxData.getLength() != -1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String next() {
		
		return "";
	}

	@Override
	public void remove() {
		
		throw new UnsupportedOperationException("Removal is not supported");
	}

	private Properties loadIfoProperties(File ifoFile) {

		Properties ifoProp = new Properties();
		InputStream fis = null;
		try {
			fis = new FileInputStream(ifoFile);
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
	
	private class IndexData {
		
		private String word;
		private int offset;
		private int length;
		
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		
	}
	
//	public static void main(String[] args) {
//		
//		File ifoFile = new File("/home/ilgo/cbeta/dictImport/mwDict/mw.ifo");
//		StardictData star = new StardictData(ifoFile);
//	}

	@Override
	public String getDefinition(String entry) {
		// TODO Auto-generated method stub
		return null;
	}
}
