package zen.ilgo.pitaka.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

import zen.ilgo.pitaka.servlet.Content;

public class JettyServer implements Runnable {

	public static final String SERVER = "http://localhost:8080/pitaka";
	private static String id = Content.class.toString();
	private static ILog log = Activator.getDefault().getLog();
	private IStatus status;
	private Server server;

	@Override
	public void run() {

		try {
			server = new Server();
			InputStream config = getResource("resources/jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(config);
			configuration.configure(server);
			server.start();
		} catch (Exception e) {
			status = new Status(IStatus.ERROR, id, "JettyServer start: "
					+ e.getMessage(), e);
			log.log(status);
		} 
	}

	private InputStream getResource(String resource) throws FileNotFoundException {

		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(resource);
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (is == null) {
			is = new FileInputStream(resource);
		}
		return is;
	}

	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			status = new Status(IStatus.ERROR, id, "JettyServer stop: "
					+ e.getMessage(), e);
			log.log(status);
		} 
	}

}
