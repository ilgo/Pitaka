package zen.ilgo.pitaka.db;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class DbCollection {

	private final CollectionManagementService colManagement;
	private final ILog log;
	private final String id;
	private IStatus status;

	public DbCollection() {
		colManagement = Session.getInstance().getCollectionManagement();
		log = Activator.getDefault().getLog();
		id = this.getClass().getCanonicalName();
	}

	/**
	 * Create a new Collection.
	 * 
	 * @param name
	 *            the eXist db path of the new Collection
	 * @return the Collection created
	 */
	public Collection createCollection(String name) {

		Collection col = null;
		try {
			col = colManagement.createCollection(name);

			status = new Status(IStatus.INFO, id, "Create Collection: " + name,
					null);

		} catch (XMLDBException e) {
			status = new Status(IStatus.ERROR, id,
					"Create Collection: " + name, e);

		} finally {
			log.log(status);
		}
		return col;
	}

	/**
	 * Remove a collection.
	 * 
	 * @param col
	 *            the Collection to be removed
	 * @return the parent Collection
	 */
	public Collection removeCollection(Collection col) {

		Collection parent = null;
		String colName = null;
		try {
			colName = decode(col.getName());
			if (colName.equals("/db/Texts") || colName.equals("/db/Users")) {
				// do not allow to delete the basic collections
				IStatus status = new Status(IStatus.WARNING, id,
						"Remove Collection: " + colName,
						new IllegalArgumentException());
				log.log(status);
			} else {
				parent = col.getParentCollection();
				colManagement.removeCollection(colName);

				status = new Status(IStatus.INFO, id, "Remove Collection: "
						+ colName, null);
			}
		} catch (XMLDBException e) {
			status = new Status(IStatus.ERROR, id, "Remove Collection: "
					+ colName, e);
		} finally {
			log.log(status);
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
	 * @param col
	 *            store resource inside this collection
	 * @param resource
	 *            the Resource to be stored
	 */
	public void storeResource(final Collection col, final File resource) {

		String path = null;
		try {
			String resourceName = resource.getName();
			if (resourceName.contains(".")) {
				resourceName = resourceName.split("\\.")[0];
			}
			XMLResource document = (XMLResource) col.createResource(
					resourceName, "XMLResource");
			document.setContent(resource);
			col.storeResource(document);

			path = decode(col.getName() + "/" + resourceName);
			status = new Status(IStatus.INFO, id, "Store Resource: " + path,
					null);

		} catch (XMLDBException e) {
			status = new Status(IStatus.ERROR, id, "Store Resource: " + path, e);
		} finally {
			log.log(status);
		}
	}

	/**
	 * Remove a Resource from a Collection
	 * 
	 * @param resource
	 *            the Resource to be removed
	 * @return the deleted Resource's Collection
	 * @throws UnsupportedEncodingException
	 */
	public Collection removeResource(Resource resource) {

		Collection parent = null;
		String path = null;
		try {
			parent = resource.getParentCollection();
			parent.removeResource(resource);

			path = decode(parent.getName() + "/" + resource.getId());
			status = new Status(IStatus.INFO, id, "Remove Resource: " + path,
					null);

		} catch (XMLDBException e) {
			status = new Status(IStatus.ERROR, id, "Remove Resource: " + path,
					e);
		} finally {
			log.log(status);
		}
		return parent;
	}

	public void renameResource(Collection col, String id) {

	}

	public void moveResource(Collection col, String id) {

	}

	private String decode(String text) {

		String decoded;
		try {
			decoded = URLDecoder.decode(text, "UTF-8");

		} catch (UnsupportedEncodingException e) {

			IStatus status = new Status(IStatus.ERROR, id,
					"Decoding : " + text, e);
			log.log(status);
			decoded = text;
		}
		return decoded;
	}
}
