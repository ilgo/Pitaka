package zen.ilgo.pitaka.db;

import java.io.File;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class DbCollection {

	private final CollectionManagementService colManagement;

	public DbCollection() {
		colManagement = Session.getInstance().getCollectionManagement();
	}

	/**
	 * Create a new Collection.
	 * 
	 * @param name the eXist db path of the new Collection
	 * @return the Collection created
	 */
	public Collection createCollection(String name) {

		Collection col = null;
		try {
			col = colManagement.createCollection(name);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return col;
	}

	/**
	 * Remove a collection.
	 * 
	 * @param col the Collection to be removed
	 * @return the parent Collection
	 */
	public Collection removeCollection(Collection col) {

		Collection parent = null;
		try {
			String root = col.getName();
			if (root.equals("/db/Texts") || root.equals("/db/Users")) {
				// do not allow to delete the basic collections
			} else {
				parent = col.getParentCollection();
				colManagement.removeCollection(root);
			}
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;
	}

	public void renameCollection(String name) {

	}

	public void moveCollection(String name) {

	}

	public Collection getCollection(String name) {

		return null;
	}

	/**
	 * Store a resource in a Collection
	 * 
	 * @param col store resource inside this collection
	 * @param resource the Resource to be stored
	 */
	public void storeResource(final Collection col, final File resource) {

		try {
			String name = resource.getName();
			if (name.contains(".")) {
				name = name.split("\\.")[0];
			}
			XMLResource document = (XMLResource) col.createResource(name,
					"XMLResource");
			document.setContent(resource);
			// System.out.print("storing document " + document.getId() +
			// "...");
			col.storeResource(document);
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove a Resource from a Collection
	 * 
	 * @param resource the Resource to be removed
	 * @return the deleted Resource's Collection
	 */
	public Collection removeResource(Resource resource) {

		Collection parent = null;
		try {	
			parent = resource.getParentCollection();
			parent.removeResource(resource);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;
	}

	public void renameResource(Collection col, String id) {

	}

	public void moveResource(Collection col, String id) {

	}
}
