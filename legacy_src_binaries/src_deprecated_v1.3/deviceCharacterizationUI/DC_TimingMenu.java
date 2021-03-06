package deviceCharacterizationUI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import simulationObjects.ResourcesAndUsefulFunctions;
import simulationObjects.Timing;
import simulationObjects.TimingAdaptative;
import simulationObjects.TimingUniform;

public class DC_TimingMenu extends Composite {
	private Text textTimingInit;
	private Text textTimingFinal;
	private Text textTimingStep;
	private Button btnDynamicStep;
	private Button btnFixedStep;
	private Text textTimingMinStep;
	private Text textTimingMaxStep;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DC_TimingMenu(Composite parent, int style) {
		super(parent, SWT.NONE);

		Group composite = new Group(this, SWT.SHADOW_ETCHED_IN);
		composite.setBounds(0, 0, 705, 166);
		composite.setText("Timing parameters");

		Label lblTinits = new Label(composite, SWT.NONE);
		lblTinits.setText("T_Init (s):");
		lblTinits.setBounds(10, 62, 75, 16);

		Label lblTfinals = new Label(composite, SWT.NONE);
		lblTfinals.setText("T_Final (s):");
		lblTfinals.setBounds(151, 62, 75, 16);

		textTimingInit = new Text(composite, SWT.BORDER);
		textTimingInit.setText("0");
		textTimingInit.setBounds(91, 56, 54, 26);

		textTimingFinal = new Text(composite, SWT.BORDER);
		textTimingFinal.setText("300e-9");
		textTimingFinal.setBounds(232, 56, 54, 26);

		textTimingStep = new Text(composite, SWT.BORDER);
		textTimingStep.setText("1e-5");
		textTimingStep.setEnabled(false);
		textTimingStep.setBounds(139, 130, 75, 26);

		btnDynamicStep = new Button(composite, SWT.RADIO);
		btnDynamicStep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textTimingStep.setEnabled(false);
				textTimingMinStep.setEnabled(true);
			}
		});
		btnDynamicStep.setSelection(true);
		btnDynamicStep.setBounds(304, 134, 204, 22);
		btnDynamicStep.setText("Dynamic step between (s):");

		btnFixedStep = new Button(composite, SWT.RADIO);
		btnFixedStep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textTimingStep.setEnabled(true);
				textTimingMinStep.setEnabled(false);
			}
		});
		btnFixedStep.setText("Fixed step (s)");
		btnFixedStep.setBounds(10, 134, 112, 22);

		textTimingMinStep = new Text(composite, SWT.BORDER);
		textTimingMinStep.setText("" + ResourcesAndUsefulFunctions.MINSTEPDC);
		textTimingMinStep.setBounds(514, 130, 61, 26);
		textTimingMaxStep = new Text(composite, SWT.BORDER);
		textTimingMaxStep.setText("" + ResourcesAndUsefulFunctions.MAXSTEPDC);
		textTimingMaxStep.setBounds(610, 130, 61, 26);

		Label lblAnd = new Label(composite, SWT.NONE);
		lblAnd.setBounds(581, 135, 29, 15);
		lblAnd.setText("and");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 39, 154, 17);
		lblNewLabel.setText("Transient duration");
		
		Label lblTimingEvolutionPreference = new Label(composite, SWT.NONE);
		lblTimingEvolutionPreference.setText("Timing evolution preferences.");
		lblTimingEvolutionPreference.setBounds(10, 107, 336, 17);
	}

	public Timing getTiming() {
		Timing timing = null;
		try {
			// retrieve timing
			if (btnDynamicStep.getSelection()) {
				timing = new TimingAdaptative(Double.parseDouble(textTimingInit
						.getText()), Double.parseDouble(textTimingFinal
						.getText()), Double.parseDouble(textTimingMinStep
						.getText()), Double.parseDouble(textTimingMaxStep
						.getText()));

			} else {
				timing = new TimingUniform(Double.parseDouble(textTimingInit
						.getText()), Double.parseDouble(textTimingFinal
						.getText()), Double.parseDouble(textTimingStep
						.getText()));
			}
			if (timing.getTStep() >= timing.getTf()) {
				MessageDialog.openWarning(new Shell(), "Parameters error",
						"Timing error: define the time range of operation");
				return null;
			}
		} catch (Exception exc) {
			MessageDialog.openWarning(new Shell(), "Parameters error",
					"Timing error: " + exc.getLocalizedMessage());
			return null;
		}
		return timing;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setUniformStep(boolean selection) {
		this.btnFixedStep.setSelection(selection);
		this.btnDynamicStep.setSelection(!selection);
		this.btnDynamicStep.setEnabled(!selection);
		this.textTimingStep.setEnabled(selection);
		this.textTimingMinStep.setEnabled(!selection);
	}

	public void setMaxStep(double newT) {
		textTimingMinStep.setText("" + newT / 10);
		textTimingMaxStep.setText("" + newT);
		textTimingStep.setText("" + newT);
	}
}
