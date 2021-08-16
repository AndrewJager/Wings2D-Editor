package com.wings2d.editor.ui.setters;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.UIPanel;

public class DoubleEditSetter<T extends UIPanel<T>> extends UIElement<T> {
	private ActionListener evt;
	
	private JFormattedTextField textField;

	public DoubleEditSetter(final T parent, final String caption,
			final ActionListener evt) {
		super(parent);
		this.evt = evt;
		
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel(caption), BorderLayout.WEST);
		
		textField = new JFormattedTextField(new DecimalFormat());
		panel.add(textField, BorderLayout.EAST);
	}
	
	public Double getAmt() {
		return Double.parseDouble(textField.getText());
	}
	public void setAmt(final double amt) {
		textField.setValue(amt);
	}

	@Override
	public void createEvents() {
		textField.addActionListener(evt);
	}
}
