package org.dict.zip;


public interface IDataAccessor {
	
	public byte[] readData(int offset, int len) throws java.io.IOException;

	public void dispose();
}
