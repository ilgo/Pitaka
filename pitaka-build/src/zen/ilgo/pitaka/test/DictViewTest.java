package zen.ilgo.pitaka.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class DictViewTest {

	private Shell shell;
	private Display display;
	private Composite dictManage;

	private List unusedList;
	private List usingList;

	public void show() {

		display = new Display();
		shell = new Shell(display);
		shell.setText("Dict Manage Test");
		shell.setBounds(150, 150, 400, 250);
		createControl(shell);
		try {
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} finally {
			display.dispose();
		}
	}

	public void createControl(Composite parent) {

		// add Controls here for a quick GUI test
	}

	public static void main(String[] args) {

		DictViewTest d = new DictViewTest();
		d.show();
	}
}
