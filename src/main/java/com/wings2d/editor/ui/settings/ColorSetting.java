package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.ui.UIElement;

public class ColorSetting extends UIElement<SettingsEdit>{
	private Supplier<Color> getter;
	private Consumer<Color> setter;
	
	private JButton editBtn;
	private JPanel indicator;
	
	public ColorSetting(final SettingsEdit parent, final String caption,
			final Supplier<Color> getter, final Consumer<Color> setter) {
		super(parent);
		this.getter = getter;
		this.setter = setter;
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel(caption), BorderLayout.WEST);
		
		JPanel setPanel = new JPanel();
		setPanel.setLayout(new BorderLayout());
		
		indicator = new JPanel();
		indicator.setPreferredSize(new Dimension(20, 20));
		indicator.setBackground(getter.get());
		indicator.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setPanel.add(indicator, BorderLayout.WEST);
		
		editBtn = new JButton("Set");
		setPanel.add(editBtn, BorderLayout.EAST);
		panel.add(setPanel, BorderLayout.EAST);
	}

	@Override
	public void createEvents() {
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(panel, "Choose Sprite color", getter.get());
				if (color != null)
				{
					setter.accept(color);
					indicator.setBackground(getter.get());
					panel.repaint(); // Fix line of previous color at bottom of indicator
				}
			}
		});
	}
}
