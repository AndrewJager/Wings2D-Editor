package com.wings2d.editor.ui.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class SettingsToolBar extends SettingsUIElement{
	private JToolBar toolbar;
	private JButton backBtn;

	public SettingsToolBar(final SettingsEdit edit) {
		super(edit);
		
		toolbar = new JToolBar();
		
		backBtn = new JButton("Back");
		toolbar.add(backBtn);
	}

	@Override
	public void createEvents() {
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settingsEdit.getEditor().showProject();
			}
		});
	}
	
	public JToolBar getToolbar() {
		return toolbar;
	}
}
