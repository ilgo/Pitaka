package zen.ilgo.pitaka.dict;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class DictManageView extends ViewPart {

	public static final String ID = "pitaka.dict.DictManageView";

	private DictManageModel model;
	private Composite dictManage;
	private List unusedList;
	private List usingList;

	@Override
	public void setFocus() {
		dictManage.setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));

		FormData formData;
		dictManage = new Composite(parent, SWT.NORMAL);
		dictManage.setLayout(new FormLayout());

		Composite returnButtons = getReturnButtonsComposite(dictManage);
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);
		returnButtons.setLayoutData(formData);

		Composite navigationButtons = getNavigationButtonsComposite(dictManage);
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(returnButtons, -5);
		navigationButtons.setLayoutData(formData);

		Composite switchList = getSwitchListComposite(dictManage);
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(navigationButtons, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(navigationButtons, -5, SWT.BOTTOM);
		switchList.setLayoutData(formData);

		dictManage.pack();

		try {
			model = new DictManageModel();
			initViewFromModel();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Composite getSwitchListComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.NORMAL);
		FormData formData;
		composite.setLayout(new FormLayout());

		unusedList = new List(composite, SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(50, -25);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		unusedList.setLayoutData(formData);

		usingList = new List(composite, SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);
		formData = new FormData();
		formData.left = new FormAttachment(50, 25);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		usingList.setLayoutData(formData);

		Button rightButton = new Button(composite, SWT.ARROW | SWT.RIGHT);
		formData = new FormData();
		formData.left = new FormAttachment(50, -20);
		formData.right = new FormAttachment(50, 20);
		formData.top = new FormAttachment(0, 40);
		rightButton.setLayoutData(formData);
		rightButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = unusedList.getSelectionIndex();
				if (idx != -1) {
					String name = unusedList.getItem(idx);
					unusedList.remove(idx);
					usingList.add(name);
					model.moveToUsing(name);
				}
			}
		});

		Button leftButton = new Button(composite, SWT.ARROW | SWT.LEFT);
		formData = new FormData();
		formData.left = new FormAttachment(50, -20);
		formData.right = new FormAttachment(50, 20);
		formData.top = new FormAttachment(rightButton, 5);
		leftButton.setLayoutData(formData);
		leftButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = usingList.getSelectionIndex();
				if (idx != -1) {
					String name = usingList.getItem(idx);
					usingList.remove(idx);
					unusedList.add(name);
					model.moveToUnused(name);
				}
			}
		});

		return composite;
	}

	private Composite getNavigationButtonsComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.NORMAL);
		FormData formData;
		composite.setLayout(new FormLayout());

		Button topButton = new Button(composite, SWT.PUSH);
		topButton.setText("Top");
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		topButton.setLayoutData(formData);
		topButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = usingList.getSelectionIndex();
				if (idx != -1) {
					String name = usingList.getItem(idx);
					usingList.remove(idx);
					usingList.add(name, 0);
					usingList.setSelection(0);
					model.moveToTop(name);
				}
			}
		});

		Button upButton = new Button(composite, SWT.PUSH);
		upButton.setText("Up");
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(topButton, 5);
		upButton.setLayoutData(formData);
		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = usingList.getSelectionIndex();
				if (idx > 0) {
					String name = usingList.getItem(idx);
					usingList.remove(idx);
					--idx;
					usingList.add(name, idx);
					usingList.setSelection(idx);
					model.moveUp(name);
				}
			}
		});

		Button downButton = new Button(composite, SWT.PUSH);
		downButton.setText("Down");
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(upButton, 5);
		downButton.setLayoutData(formData);
		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = usingList.getSelectionIndex();
				int last = usingList.getItemCount() - 1;
				if (idx < last) {
					String name = usingList.getItem(idx);
					usingList.remove(idx);
					++idx;
					usingList.add(name, idx);
					usingList.setSelection(idx);
					model.moveDown(name);
				}

			}
		});

		Button bottomButton = new Button(composite, SWT.PUSH);
		bottomButton.setText("Bottom");
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(downButton, 5);
		bottomButton.setLayoutData(formData);
		bottomButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int idx = usingList.getSelectionIndex();
				int last = usingList.getItemCount() - 1;
				if (idx < last) {
					String name = usingList.getItem(idx);
					usingList.remove(idx);
					usingList.add(name, last);
					usingList.setSelection(last);
					model.moveToBottom(name);
				}
			}
		});

		return composite;
	}

	private Composite getReturnButtonsComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.NORMAL);
		FormData formData;
		composite.setLayout(new FormLayout());

		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText("Ok");
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.left = new FormAttachment(100, -70);
		formData.bottom = new FormAttachment(100, -5);
		okButton.setLayoutData(formData);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				model.pesistSettings();
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IViewPart view = page.findView("pitaka.dict.DictManageView");
				page.hideView(view);
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText("Cancel");
		formData = new FormData();
		formData.right = new FormAttachment(okButton, -5);
		formData.left = new FormAttachment(100, -150);
		formData.bottom = new FormAttachment(100, -5);
		cancelButton.setLayoutData(formData);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				model.ignoreSettings();
				initViewFromModel();
			}
		});

		return composite;
	}

	private void initViewFromModel() {

		usingList.removeAll();
		unusedList.removeAll();
		usingList.setItems(model.getUsingDicts());
		unusedList.setItems(model.getUnusedDicts());
	}
}
