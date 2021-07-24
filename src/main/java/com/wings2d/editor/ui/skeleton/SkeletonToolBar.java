package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

public class SkeletonToolBar extends SkeletonUIElement{
	private JToolBar toolbar;
	private JButton backButton, undoBtn, redoBtn;
	private JLabel header;

	public SkeletonToolBar(final SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		toolbar = new JToolBar();
		toolbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		backButton = new JButton("Back");
		toolbar.add(backButton);
		JSeparator left = new JSeparator();
		left.setUI(null);
		toolbar.add(left);
		header = new JLabel("Skeleton", JLabel.CENTER);
		header.setFont(header.getFont().deriveFont(Font.BOLD, 20.0f));
		toolbar.add(header);
		JSeparator right = new JSeparator();
		right.setUI(null);
		toolbar.add(right);
		
		undoBtn = new JButton("Undo");
		toolbar.add(undoBtn);
		redoBtn = new JButton("Redo");
		toolbar.add(redoBtn);
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
	
	public JToolBar getToolbar() {
		return toolbar;
	};
}
