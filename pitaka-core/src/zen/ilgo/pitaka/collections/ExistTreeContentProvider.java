package zen.ilgo.pitaka.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

public class ExistTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {

		List<Collection> colChildren = new ArrayList<Collection>();
		List<Resource> resChildren = new ArrayList<Resource>();
		try {
			if (parentElement instanceof Collection) {
				Collection col = (Collection) parentElement;

				for (String name : col.listChildCollections()) {
					if (!name.equals("system")) {
						colChildren.add(col.getChildCollection(name));
					}
				}
				for (String id : col.listResources()) {
					resChildren.add(col.getResource(id));
				}
			}
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(colChildren, new Comparator<Collection>() {

			@Override
			public int compare(Collection arg0, Collection arg1) {
				int result = 0;
				try {
					String name0 = arg0.getName();
					String name1 = arg1.getName();
					result = name0.compareTo(name1);

				} catch (XMLDBException e) {

					e.printStackTrace();
				}
				return result;
			}
		});

		Collections.sort(resChildren, new Comparator<Resource>() {

			@Override
			public int compare(Resource arg0, Resource arg1) {
				int result = 0;
				try {
					String id0 = arg0.getId();
					String id1 = arg1.getId();
					result = id0.compareTo(id1);

				} catch (XMLDBException e) {

					e.printStackTrace();
				}
				return result;
			}

		});
		Object[] children = new Object[colChildren.size() + resChildren.size()];
		int idx = 0;
		for (Collection col : colChildren) {
			children[idx++] = col;
		}
		for (Resource res : resChildren) {
			children[idx++] = res;
		}
		return children;
	}

	@Override
	public Object getParent(final Object element) {

		Collection parent = null;
		try {
			if (element instanceof Collection) {
				parent = ((Collection) element).getParentCollection();
			} else if (element instanceof Resource) {
				parent = ((Resource) element).getParentCollection();
			}
		} catch (XMLDBException e) {

		}
		return parent;
	}

	@Override
	public boolean hasChildren(final Object element) {

		boolean result = false;
		if (element instanceof Resource) {
			result = false;
		}
		if (element instanceof Collection) {
			int count = 0;
			Collection col = (Collection) element;
			try {
				count += col.getChildCollectionCount();
				count += col.getResourceCount();
			} catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = count == 0 ? false : true;
		}
		return result;

	}

	@Override
	public Object[] getElements(Object inputElement) {

		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}
}
