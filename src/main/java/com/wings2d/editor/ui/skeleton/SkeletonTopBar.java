package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SkeletonTopBar extends SkeletonUIElement{
	private JButton backButton;
	private JLabel header;

	public SkeletonTopBar(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		backButton = new JButton("Back");
		panel.add(backButton);
		header = new JLabel("Skeleton", JLabel.CENTER);
		panel.add(header);
	}
	
	public void setHeaderText(final String text)
	{
		header.setText(text);
	}

	@Override
	public void createEvents() {
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getEditor().showProject();
			}
		});
	}
}
