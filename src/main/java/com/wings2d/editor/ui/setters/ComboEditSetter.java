package com.wings2d.editor.ui.setters;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.UIPanel;

public class ComboEditSetter<T extends UIPanel<T>, C> extends UIElement<T> {
	private ActionListener evt;
	
	private JComboBox<C> comboBox;

	public ComboEditSetter(final T parent, final String caption,
			final ActionListener evt) {
		super(parent);
		this.evt = evt;
		
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel(caption), BorderLayout.WEST);
		
		comboBox = new JComboBox<C>();
		panel.add(comboBox, BorderLayout.EAST);
		
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)comboBox.getPreferredSize().getHeight()));
	}
	
	public C getValue() {
		return comboBox.getItemAt(comboBox.getSelectedIndex());
	}
	public void setValue(final C itm) {
		comboBox.setSelectedItem(itm);
	}
	public void setModel(final ComboBoxModel<C> model) {
		comboBox.setModel(model);
	}

	@Override
	public void createEvents() {
		comboBox.addActionListener(evt);
	}
}
