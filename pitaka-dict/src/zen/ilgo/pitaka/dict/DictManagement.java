package zen.ilgo.pitaka.dict;

public class DictManagement {

	private static DictManagement instance;
	
	private DictManagement() {
		
	}
	
	public static DictManagement getInstance() {
		if (instance == null) {
			instance = new DictManagement();
		}
		return instance;
	}
	
	public void importDict(IDict dict) {
		
	}
	
	public void removeDict(String name) {
		
	}
}
