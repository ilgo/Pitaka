package zen.ilgo.pitaka.db;


public class StoreExample {

    public final static String URI = "xmldb:exist:///db/";

    @SuppressWarnings("unchecked")
    public static void main(String args[]) throws Exception {

        String collection = "data/test";
        String file = "/home/ilgo/Books/cbtxt/T01/T01n0011.xml";

        // initialize driver
        String driver = "org.exist.xmldb.DatabaseImpl";
        Class cl = Class.forName(driver);
//        Database database = (Database) cl.newInstance();
//        DatabaseManager.registerDatabase(database);
//
//        // try to get collection
//        Collection col = DatabaseManager.getCollection(URI + "test");
//        if (col == null) {
//            // collection does not exist: get root collection and create
//            // for simplicity, we assume that the new collection is a
//            // direct child of the root collection, e.g. /db/test.
//            // the example will fail otherwise.
//            Collection root = DatabaseManager.getCollection(URI);
//            CollectionManagementService mgtService = (CollectionManagementService) root.getService(
//                    "CollectionManagementService", "1.0");
//            col = mgtService.createCollection(collection.substring("test".length()));
//        }
		// // create new XMLResource; an id will be assigned to the new resource
		// XMLResource document = (XMLResource) col.createResource("T01n0011",
		// "XMLResource");
		// File f = new File(file);
		// if (!f.canRead()) {
		// System.out.println("cannot read file " + file);
		// return;
		// }
		// document.setContent(f);
		// System.out.print("storing document " + document.getId() + "...");
		// col.storeResource(document);
		// System.out.println("ok.");

        System.exit(0);
    }
}