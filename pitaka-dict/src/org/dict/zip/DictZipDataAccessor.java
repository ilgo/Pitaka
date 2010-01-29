package org.dict.zip;

import java.io.File;
import java.io.IOException;

public class DictZipDataAccessor implements org.dict.zip.IDataAccessor {

	private DictZipHeader header;
	private RandomAccessInputStream in = null;
	private DictZipInputStream din = null;

	public DictZipDataAccessor(File file) throws IOException, DictzipException {

		in = new RandomAccessInputStream(file, "r");
		if (file.getName().endsWith(".dz")) {

			throw new DictzipException(file.getName() + ": Decompress with dictzip tool");
			// din = new DictZipInputStream(in);
			// header = din.readHeader();
		}
	}

	@Override
	public byte[] readData(int start, int len) {

		byte[] bytes = null;
		try {
			if (din != null) {
				int idx = start / header.chunkLength;
				int off = start % header.chunkLength;
				int pos = header.offsets[idx];
				in.seek(pos);
				bytes = new byte[off + len];
				din.readFully(bytes);

			} else {
				in.seek(start);
				bytes = new byte[len];
				in.readFully(bytes);
			}

		} catch (java.io.IOException e) {

			bytes = new byte[0];
		}
		return bytes;
	}

	@Override
	public void dispose() {

		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {

			}
		}
		if (din != null) {
			try {
				din.close();
			} catch (IOException e) {

			}
		}
	}
}
