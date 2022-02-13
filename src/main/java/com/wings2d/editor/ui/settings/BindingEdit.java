package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.wings2d.editor.objects.project.EditorKeyBind;
import com.wings2d.framework.misc.KeyBind;

public class BindingEdit extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JTextField text;
	private JButton reset;

	public BindingEdit(final String name, final EditorKeyBind keyBind) {
		super();
		
		this.setLayout(new BorderLayout());
		
		label = new JLabel(name + ":");
		this.add(label, BorderLayout.WEST);
		
		text = new JTextField();
		text.setEditable(false);
		text.setPreferredSize(new Dimension(100, 20));
		text.setText(keyBind.getBinding().getValue());
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				boolean hasKey = false;
				for (int i = 0; i < keyBind.getBinding().getKeys().size(); i++) {
					if (keyBind.getBinding().getKeys().get(i).getValue().equals(String.valueOf(e.getKeyChar()))) {
						hasKey = true;
						break;
					}
				}
				if (!hasKey) {
					keyBind.getBinding().addKey(new KeyBind(KeyEvent.getKeyText(e.getKeyCode())));
					
				}
				text.setText(keyBind.getBinding().getValue());
				keyBind.setKeys(keyBind.getBinding().getValue());
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});
		JPanel textPanel = new JPanel();
		textPanel.add(text);
		this.add(textPanel, BorderLayout.CENTER);
		
		reset = new JButton("Clear");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyBind.getBinding().getKeys().clear();
				text.setText(keyBind.getBinding().getValue());
			}
		});
		this.add(reset, BorderLayout.EAST);
	}
}
