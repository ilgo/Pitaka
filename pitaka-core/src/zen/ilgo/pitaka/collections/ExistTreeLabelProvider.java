package zen.ilgo.pitaka.collections;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import zen.ilgo.pitaka.core.Application;

public class ExistTreeLabelProvider implements ILabelProvider {

	private Map<String, Image> iconMap;

	Image colImg;
	Image resImg;
	Image errImg;

	public ExistTreeLabelProvider() {

		// Session session = Session.getInstance();
		Device device = Application.DISPLAY;

		// File file = new File(".");
		// System.out.println(file.getAbsolutePath());

		ClassLoader cl = this.getClass().getClassLoader();
		String[] iconNames = { "database", "database_edit", "book",
				"book_edit", "book_open", "book_open_edit", "folder", "user",
				"exclamation" };
		iconMap = new HashMap<String, Image>();
		for (String iconName : iconNames) {

			String icon = "icons/" + iconName + ".png";
			// InputStream is = session.getResource(icon);
			InputStream is = cl.getResourceAsStream(icon);
			if (is == null) {
				is = ClassLoader.getSystemResourceAsStream(icon);
			}
			if (is == null) {
				try {
					is = new FileInputStream(icon);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ImageData data = new ImageData(is);
			Image img = new Image(device, data);
			iconMap.put(iconName, img);
		}
	}

	@Override
	public Image getImage(Object element) {

		Image img = null;
		try {
			if (element instanceof Collection) {
				String name = ((Collection) element).getName();
				if (name.equals("/db/Users")) {
					img = iconMap.get("database_edit");
				} else if (name.contains("User")) {
					img = iconMap.get("user");
				} else if (name.equals("/db/Texts")) {
					img = iconMap.get("database");
				} else {
					img = iconMap.get("folder");
				}

			} else if (element instanceof Resource) {
				Collection parent = ((Resource) element).getParentCollection();
				if (parent.getName().contains("User")) {
					img = iconMap.get("book_edit");
				} else {
					img = iconMap.get("book");
				}
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
			img = iconMap.get("exclamation");
		}
		return img;
	}

	@Override
	public String getText(Object element) {

		String name = "";
		try {
			if (element instanceof Collection) {
				String[] parts = ((Collection) element).getName().split("/");
				name = parts[parts.length - 1];

			} else if (element instanceof Resource) {
				name = ((Resource) element).getId();
			}
			name = URLDecoder.decode(name, "UTF-8");
		} catch (XMLDBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

		for (Image img : iconMap.values()) {
			if (img != null) {
				img.dispose();
			}
		}

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
