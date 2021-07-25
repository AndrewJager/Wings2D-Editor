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
	
	private JSpinner handleSize, posHandleOffset, rotHandleOffset;

	public DrawingSettings(final SettingsEdit edit, final EditorSettings settings) {
		super(edit);
		this.settings = settings;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel handleSizePanel = new JPanel();
		handleSizePanel.add(new JLabel("Handle Size:"));
		SpinnerModel handleModel = new SpinnerNumberModel(settings.getHandleSize(), 0, 1000, 1);
		handleSize = new JSpinner(handleModel);
		handleSizePanel.add(handleSize);
		panel.add(handleSizePanel);
		
		JPanel posHandleOffsetPanel = new JPanel();
		posHandleOffsetPanel.add(new JLabel("Position Edit Handle Offset:"));
		handleModel = new SpinnerNumberModel(settings.getPosHandleOffset(), 0, 1000, 1);
		posHandleOffset = new JSpinner(handleModel);
		posHandleOffsetPanel.add(posHandleOffset);
		panel.add(posHandleOffsetPanel);
		
		JPanel rotHandleOffsetPanel = new JPanel();
		rotHandleOffsetPanel.add(new JLabel("Rotation Edit Handle Offset:"));
		handleModel = new SpinnerNumberModel(settings.getRotHandleOffset(), 0, 1000, 1);
		rotHandleOffset = new JSpinner(handleModel);
		rotHandleOffsetPanel.add(rotHandleOffset);
		panel.add(rotHandleOffsetPanel);
	}

	@Override
	public void createEvents() {
		handleSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setHandleSize((int)handleSize.getValue());
			}
		});
		posHandleOffset.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setPosHandleOffset((int)posHandleOffset.getValue());
			}
		});
		rotHandleOffset.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setRotHandleOffset((int)rotHandleOffset.getValue());
			}
		});
	}
}
