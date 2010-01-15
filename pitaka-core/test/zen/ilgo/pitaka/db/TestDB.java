package zen.ilgo.pitaka.db;


public class TestDB {
    @SuppressWarnings("unchecked")
    public static void main(String args[]) throws Exception {
        // initialize driver
        Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
        //Database database = (Database) cl.newInstance();
        //database.setProperty("create-database", "true");
		// DatabaseManager.registerDatabase(database);
		//
		// // try to read collection
		// // String uri = System.getProperty("exist.home");
		// // System.out.println("URI: " + uri);
		// try {
		// Collection col =
		// DatabaseManager.getCollection("xmldb:exist:///db/test", "admin",
		// "admin");
		//
		// String resources[] = col.listResources();
		// System.out.println("Resources:");
		// for (int i = 0; i < resources.length; i++) {
		// System.out.println(resources[i]);
		// }
		//
		// Resource res = col.getResource(resources[0]);
		// System.out.println(((XMLResource) res).getContent());
		//
		// XPathQueryService service = (XPathQueryService)
		// col.getService("XPathQueryService",
		// "1.0");
		// // XQueryService service = (XQueryService) col.getService(
		// "XQueryService", "1.0" );
		// // service.setProperty(OutputKeys.ENCODING, "UTF-8");
		// // String query = String.format("doc(\"%s\")//title", resources[2]);
		// ResourceSet result = service.queryResource(resources[0],
		// "/TEI[@xml:id]");
		// System.out.println(result.getSize());
		//
		// // shut down the database
		// DatabaseInstanceManager manager = (DatabaseInstanceManager)
		// col.getService(
		// "DatabaseInstanceManager", "1.0");
		// manager.shutdown();
		// } catch (Exception e) {
		// System.out.println(e.getCause());
		// e.printStackTrace(System.out);
		// }
    }
}