package zen.ilgo.pitaka.db;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.transform.OutputKeys;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XPathQueryService;

/**
 * holds objects that need to be shared.
 * 
 * in order to reinitalize the eXist-DB pass -Dexist.create=true to the JVM.
 * 
 * @author ilgo
 * 
 */
public class Session {

	public static final String DB_URL = "xmldb:exist:///db";
	public static final String LOCATION = "/home/ilgo/tmp/pitaka";

	private final ClassLoader cl;
	private static Session session;
	private CollectionManagementService collectionManagement;
	// private XPathQueryService xPathQueryService;
	private Collection rootCollection;

	private Session() {

		cl = this.getClass().getClassLoader();
		System.setProperty("exist.home", Session.LOCATION + "/db");
		System.setProperty("exist.initdb", "true");
		initDB();
	}

	public static Session getInstance() {
		if (session == null) {
			session = new Session();
		}
		return session;
	}

	public CollectionManagementService getCollectionManagement() {
		return collectionManagement;
	}

	public Collection getRootCollection() {
		return rootCollection;
	}

	public XPathQueryService getQueryService(Collection col) {

		XPathQueryService xPathQueryService = null;
		try {
			xPathQueryService = (XPathQueryService) col.getService(
					"XPathQueryService", "1.0");
			xPathQueryService.setProperty(OutputKeys.INDENT, "yes");
			xPathQueryService.setProperty(OutputKeys.ENCODING, "UTF-8");
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xPathQueryService;
	}

	public InputStream getResource(String resource) {
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

	private void initDB() {

		try {

			if (System.getProperty("exist.create") != null) {

				System.out.println("creating eXist DB @ " + Session.LOCATION
						+ "/db");

				removeDB(Session.LOCATION);
				createDB(Session.LOCATION + "/db");

				initDBConnection();

				collectionManagement.createCollection(Session.DB_URL
						+ "/Texts/Pitaka");
				collectionManagement.createCollection(Session.DB_URL
						+ "/Users");

			} else {
				initDBConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDBConnection() throws Exception {
		Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);
		rootCollection = DatabaseManager.getCollection(Session.DB_URL);
		collectionManagement = (CollectionManagementService) rootCollection
				.getService("CollectionManagementService", "1.0");
	}

	private void createDB(String location) throws Exception {

		String dataLocation = location + "/data";
		new File(dataLocation).mkdirs();
		new File(location + "/logs").mkdirs();
		copyConfXml(location);
	}

	private void removeDB(String dataLocation) {
		File dataDir = new File(dataLocation);
		if (dataDir.exists()) {
			walkAndDelete(dataDir);
		}
	}

	private void copyConfXml(String location) throws Exception {

		ClassLoader cl = Session.class.getClassLoader();
		System.out.println(cl.toString());

		InputStream conf = cl.getResourceAsStream("resources/conf.xml");
		if (conf == null) {
			conf = new FileInputStream(new File(".", "resources/conf.xml"));
			if (conf == null) {
				System.out.println("Error reading conf.xml");
				System.exit(1);
			}
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(conf);
		Node node = doc.selectSingleNode("//cluster");
		((Element) node).attribute("journalDir").setValue(location + "/logs");
		node = doc.selectSingleNode("//db-connection");
		((Element) node).attribute("files").setValue(location + "/data");
		node = doc.selectSingleNode("//recovery");
		((Element) node).attribute("journal-dir").setValue(location + "/data");
		node = doc.selectSingleNode("//catalog");
		((Element) node).attribute("uri").setValue(location + "/catalog.xml");
		File outFile = new File(location, "conf.xml");
		Writer out = new FileWriter(outFile);
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer;
		writer = new XMLWriter(out, format);
		writer.write(doc);
		writer.close();
	}

	private void walkAndDelete(File path) {

		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				walkAndDelete(file);
			} else {
				file.delete();
			}
		}
		path.delete();
	}

	@SuppressWarnings("unused")
	private String readXml(String location) {

		File file = new File(location);
		int len = (int) file.length();
		int read = 0;
		byte[] buf = new byte[len];
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			read = bis.read(buf);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
		}
		System.out.println(location + " -- " + read);
		return new String(buf);
	}
}
