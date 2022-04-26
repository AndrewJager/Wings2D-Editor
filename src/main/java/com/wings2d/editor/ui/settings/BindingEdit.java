package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.wings2d.editor.objects.project.EditorKeyBind;

public class BindingEdit extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JTextField text;
	private JToggleButton ctrl, shift, alt;

	public BindingEdit(final String name, final EditorKeyBind keyBind) {
		super();
		
		this.setLayout(new BorderLayout());
		
		label = new JLabel(name + ":");
		this.add(label, BorderLayout.WEST);

		JPanel textPanel = new JPanel();
		text = new JTextField();
		text.setEditable(false);
		text.setPreferredSize(new Dimension(40, 20));
		text.setHorizontalAlignment(JTextField.CENTER);
		text.setText(keyBind.getKey());
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				text.setText(KeyEvent.getKeyText(e.getKeyCode()));
				keyBind.setKey(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});
		ctrl = new JToggleButton("CTRL");
		ctrl.setSelected(keyBind.getCtrl());
		ctrl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyBind.setCtrl(ctrl.isSelected());
				shift.setSelected(false);
				keyBind.setShift(false);
				alt.setSelected(false);
				keyBind.setAlt(false);
			}
		});
		textPanel.add(ctrl);
		shift = new JToggleButton("SHIFT");
		shift.setSelected(keyBind.getShift());
		shift.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyBind.setShift(shift.isSelected());
				ctrl.setSelected(false);
				keyBind.setCtrl(false);
				alt.setSelected(false);
				keyBind.setAlt(false);
			}
		});
		textPanel.add(shift);
		alt = new JToggleButton("ALT");
		alt.setSelected(keyBind.getAlt());
		alt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyBind.setAlt(alt.isSelected());
				ctrl.setSelected(false);
				keyBind.setCtrl(false);
				shift.setSelected(false);
				keyBind.setShift(false);
			}
		});
		textPanel.add(alt);
		
		textPanel.add(text);
		this.add(textPanel, BorderLayout.EAST);
	}
}
