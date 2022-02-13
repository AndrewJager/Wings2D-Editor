package com.wings2d.editor.ui.setters;

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
import com.wings2d.editor.ui.UIPanel;

public class IntSetter<T extends UIPanel<T>> extends UIElement<T>{
	private IntConsumer setter;
	
	private JSpinner spinner;
	
	public IntSetter(final T parent, final String caption,
			final IntSupplier getter, final IntConsumer setter) {
		this(parent, caption, getter, setter, 1);
	}
	public IntSetter(final T parent, final String caption,
			final IntSupplier getter, final IntConsumer setter, final int step) {
		super(parent);
		this.setter = setter;
		
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel(caption), BorderLayout.WEST);
		
		SpinnerModel handleModel = new SpinnerNumberModel(getter.getAsInt(), 0, 1000, step);
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
