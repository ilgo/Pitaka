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

	public void createCollection(String name) {

		// if (object instanceof Collection) {
		// try {
		//
		// CreateCollectionDialog dialog = new CreateCollectionDialog(
		// HandlerUtil.getActiveWorkbenchWindow(event)
		// .getShell());
		// dialog.create();
		// if (dialog.open() == Window.OK) {
		// String colName = dialog.getCollectionName();
		// if (colName != null) {
		// String root = ((Collection) object).getName();
		// dbCol.createCollection(root + "/" + colName);
		// refresh(object);
		// }
		// }
		//
		// String root = ((Collection) object).getName();
		// dbCol.createCollection(root + "/"
		// + dialog.getCollectionName());
		// refresh(object);
		// } catch (XMLDBException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// try {
		// colManagement.createCollection(name);
		// } catch (XMLDBException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void removeCollection(Object object) {
		
//		if (object instanceof Collection) {
//			try {				
//				String root = ((Collection) object).getName();
//				if (root.equals("/db/Texts") || root.equals("/db/Users")) {
//					// do not allow to delete the basic collections
//				} else {
//					Collection parent = getParent(object);
//					dbCol.removeCollection(root);
//					refresh(parent);
//				}
//			} catch (XMLDBException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		try {
//			colManagement.removeCollection(name);
//		} catch (XMLDBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void renameCollection(String name) {

	}

	public void moveCollection(String name) {

	}

	public Collection getCollection(String name) {

		
		return null;
	}

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

	public void removeResource(final Collection col, final String id) {

		try {
			Resource resource = col.getResource(id);
			col.removeResource(resource);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void renameResource(Collection col, String id) {

	}

	public void moveResource(Collection col, String id) {

	}
	
	public Collection getParent(Object obj) {

		Collection parent = null;
		try {
			if (obj instanceof Collection) {
				parent = ((Collection) obj).getParentCollection();
			} else if (obj instanceof Resource) {
				parent = ((Resource) obj).getParentCollection();
			}
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;
	}

}
