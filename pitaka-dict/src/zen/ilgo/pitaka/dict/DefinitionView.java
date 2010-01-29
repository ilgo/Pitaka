package zen.ilgo.pitaka.dict;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class DefinitionView extends ViewPart {

	public static final String ID = "pitaka.dict.DefinitionView";
	private static Text textArea;

	private Font font;
	private Color color;

	@Override
	public void createPartControl(Composite parent) {
		textArea = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.LEFT
				| SWT.V_SCROLL | SWT.WRAP);

		FontData fd = new FontData("AR PL UKai TW", 12, SWT.NORMAL);
		Device device = parent.getDisplay();
		font = new Font(device, fd);
		textArea.setFont(font);
		color = new Color(device, 137, 93, 36);
		textArea.setForeground(color);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		color.dispose();
		font.dispose();
	}

	public static void setText(String text) {
		textArea.setText(text);
	}

}
