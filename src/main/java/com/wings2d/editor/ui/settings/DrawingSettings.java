package com.wings2d.editor.ui.settings;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.wings2d.editor.objects.EditorSettings;

public class DrawingSettings extends SettingsUIElement{
	private EditorSettings settings;
	
	private JPanel handleSizePanel;
	private JSpinner handleSize;

	public DrawingSettings(final SettingsEdit edit, final EditorSettings settings) {
		super(edit);
		this.settings = settings;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		handleSizePanel = new JPanel();
		handleSizePanel.add(new JLabel("Handle Size:"));
		SpinnerModel handleModel = new SpinnerNumberModel(settings.getHandleSize(), 0, 1000, 1);
		handleSize = new JSpinner(handleModel);
		handleSizePanel.add(handleSize);
		panel.add(handleSizePanel);
	}

	@Override
	public void createEvents() {
		handleSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setHandleSize((int)handleSize.getValue());
			}
		});
	}
}
