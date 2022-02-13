package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wings2d.framework.misc.ActionBinding;
import com.wings2d.framework.misc.KeyBind;

public class BindingEdit extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JTextField text;
	private JButton reset;
	
	private ActionBinding binding;

	public BindingEdit(final String name) {
		super();
		binding = new ActionBinding();
		
		this.setLayout(new BorderLayout());
		
		label = new JLabel(name + ":");
		this.add(label, BorderLayout.WEST);
		
		text = new JTextField();
		text.setEditable(false);
		text.setPreferredSize(new Dimension(100, 20));
		text.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				boolean hasKey = false;
				for (int i = 0; i < binding.getBindings().size(); i++) {
					if (binding.getBindings().get(i).getValue().equals(String.valueOf(e.getKeyChar()))) {
						hasKey = true;
						break;
					}
				}
				if (!hasKey) {
					binding.addBinding(new KeyBind(KeyEvent.getKeyText(e.getKeyCode())));
					
				}
				text.setText(binding.getValue());
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
				binding.getBindings().clear();
				text.setText(binding.getValue());
			}
		});
		this.add(reset, BorderLayout.EAST);
	}
}
