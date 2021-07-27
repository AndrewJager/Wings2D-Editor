package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import com.wings2d.editor.ui.UIElement;

public class SkeletonToolBar extends UIElement<SkeletonEdit>{
	private JToolBar toolbar;
	private JButton backButton, undoBtn, redoBtn, saveBtn;
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
		header = new JLabel("Header", JLabel.CENTER);
		header.setFont(header.getFont().deriveFont(Font.BOLD, 20.0f));
		toolbar.add(header);
		JSeparator right = new JSeparator();
		right.setUI(null);
		toolbar.add(right);
		
		undoBtn = new JButton("Undo");
		toolbar.add(undoBtn);
		redoBtn = new JButton("Redo");
		toolbar.add(redoBtn);
		saveBtn = new JButton("Save");
		toolbar.add(saveBtn);
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
				getEditPanel().getEditor().showProject();
			}
		});
		undoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getEditor().getEditsManager().undo();
			}
		});
		redoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getEditor().getEditsManager().redo();
			}
		});
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Not implemented yet");
			}
		});
	}
	
	public JToolBar getToolbar() {
		return toolbar;
	};
}
