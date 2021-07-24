package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SkeletonTopBar extends SkeletonUIElement{
	private JButton backButton, undoBtn, redoBtn;
	private JLabel header;

	public SkeletonTopBar(final SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		backButton = new JButton("Back");
		panel.add(backButton);
		header = new JLabel("Skeleton", JLabel.CENTER);
		panel.add(header);
		
		undoBtn = new JButton("Undo");
		panel.add(undoBtn);
		redoBtn = new JButton("Redo");
		panel.add(redoBtn);
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
		undoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getEditor().getEditsManager().undo();
			}
		});
		redoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getEditor().getEditsManager().redo();
			}
		});
	}
}
