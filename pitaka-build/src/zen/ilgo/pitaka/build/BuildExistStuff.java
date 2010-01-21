package zen.ilgo.pitaka.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class BuildExistStuff {

	public final String DB_URL;
	public final String EXIST_HOME;

	public BuildExistStuff() {

		DB_URL = "xmldb:exist:///db";
		EXIST_HOME = System.getProperty("exist.home");

		try {
			copyConfXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void copyConfXml() throws Exception {

		String home = System.getProperty("exist.project.dir");
		InputStream conf = new FileInputStream(new File(home,
				"resources/conf.xml"));
		if (conf == null) {
			throw new FileNotFoundException("resources/conf.xml not found");
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(conf);
		Node node = doc.selectSingleNode("//cluster");
		((Element) node).attribute("journalDir").setValue(EXIST_HOME + "/logs");
		node = doc.selectSingleNode("//db-connection");
		((Element) node).attribute("files").setValue(EXIST_HOME + "/data");
		node = doc.selectSingleNode("//recovery");
		((Element) node).attribute("journal-dir")
				.setValue(EXIST_HOME + "/data");
		node = doc.selectSingleNode("//catalog");
		((Element) node).attribute("uri").setValue(EXIST_HOME + "/catalog.xml");
		File outFile = new File(EXIST_HOME, "conf.xml");
		Writer out = new FileWriter(outFile);
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer;
		writer = new XMLWriter(out, format);
		writer.write(doc);
		writer.close();
	}

	public static void main(String[] args) {
		new BuildExistStuff();
		System.exit(0);
	}
}
