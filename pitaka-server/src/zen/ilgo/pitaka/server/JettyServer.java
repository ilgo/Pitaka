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

public class JettyServer {

	public static final String SERVER = "http://localhost:8080/pitaka";
	private static String id = Content.class.toString();
	private static ILog log = Activator.getDefault().getLog();
	private static Server server;

	private static JettyServer jetty;

	private JettyServer() {
		server = new Server();
	}

	public static JettyServer getInstance() {
		if (jetty == null) {
			jetty = new JettyServer();
		}
		return jetty;
	}

	private static InputStream getResource(String resource)
			throws FileNotFoundException {

		ClassLoader cl = JettyServer.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(resource);
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream(resource);
		}
		if (is == null) {
			is = new FileInputStream(resource);
		}
		return is;
	}

	public void start() {

		IStatus status = null;
		try {
			InputStream config = getResource("resources/jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(config);
			configuration.configure(server);
			server.start();
			status = new Status(IStatus.INFO, id, "JettyServer started.");
		} catch (Exception e) {
			status = new Status(IStatus.ERROR, id, "JettyServer start: "
					+ e.getMessage(), e);
		} finally {
			log.log(status);
		}
	}

	public void stop() {
		
		IStatus status = null;
		try {
			server.stop();
			//server = null;
			status = new Status(IStatus.INFO, id, "JettyServer stopped.");
		} catch (Exception e) {
			status = new Status(IStatus.ERROR, id, "JettyServer stop: "
					+ e.getMessage(), e);			
		} finally {
			log.log(status);
		}		
	}
}
