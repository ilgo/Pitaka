package zen.ilgo.pitaka.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

public class StartServer implements Runnable {

	public static final String SERVER = "http://localhost:8080/pitaka";
	
	@Override
	public void run() {

		try {			
			Server server = new Server();
			InputStream config = getResource("resources/jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(config);
			configuration.configure(server);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private InputStream getResource(String resource) {
		
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(resource);
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (is == null) {
			try {
				is = new FileInputStream(resource);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is;
	}

}
