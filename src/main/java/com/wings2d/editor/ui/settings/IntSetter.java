package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.wings2d.editor.ui.UIElement;

public class IntSetter extends UIElement<SettingsEdit>{
	private IntConsumer setter;
	
	private JSpinner spinner;

	public IntSetter(final SettingsEdit parent, final String caption,
			final IntSupplier getter, final IntConsumer setter) {
		super(parent);
		this.setter = setter;
		
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel(caption), BorderLayout.WEST);
		
		SpinnerModel handleModel = new SpinnerNumberModel(getter.getAsInt(), 0, 1000, 1);
		spinner = new JSpinner(handleModel);
		panel.add(spinner, BorderLayout.EAST);
	}

	@Override
	public void createEvents() {
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setter.accept((int)spinner.getValue());
			}
		});
	}
}
